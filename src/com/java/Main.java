import api.*;
import com.google.gson.Gson;
import util.BuildGraph;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            ApiClient apiClient = new ApiClient("https://gtm.delary.dev");
            String userId = "usuario"; // Replace with actual user ID

            String mazesJson = apiClient.getRequest("/labirintos");
            String[] mazes = new Gson().fromJson(mazesJson, String[].class);

            if (mazes != null) {
                System.out.println("Lista de labirintos:");
                for (int i = 0; i < mazes.length; i++) {
                    System.out.println((i + 1) + ". " + mazes[i]);
                }
                String selectedMaze = selectMaze(mazes);
                BuildGraph buildGraph = new BuildGraph(apiClient, userId, selectedMaze);
                Map<Integer, List<Integer>> adjacencyList = buildGraph.getAdjacencyList();
                printAdjacencyList(adjacencyList);
            } else {
                System.out.println("Nenhum labirinto encontrado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void printAdjacencyList(Map<Integer, List<Integer>> adjacencyList) {
        adjacencyList.forEach((node, edges) -> System.out.println("Node " + node + " -> " + edges));
    }

    private static String selectMaze(String[] mazes) {
        Scanner scanner = new Scanner(System.in);
        int selection = -1;
        while (selection < 1 || selection > mazes.length) {
            System.out.println("Select a maze number (1-" + mazes.length + "):");
            if (scanner.hasNextInt()) {
                selection = scanner.nextInt();
                if (selection < 1 || selection > mazes.length) {
                    System.out.println("Invalid selection. Please select a valid maze number.");
                }
            } else {
                scanner.next(); // Consume the invalid input
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        scanner.close(); // It's a good practice to close the scanner when it's no longer needed
        return mazes[selection - 1]; // Array index starts at 0
    }
}
