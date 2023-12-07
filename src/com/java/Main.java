import api.ApiClient;
import api.ApiResponse;
import service.MazeSolver;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        MazeSolver solver;
        try {
            solver = new MazeSolver(new ApiClient("https://gtm.delary.dev"));
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            List<String> labyrinthList = solver.getMazes();
            System.out.println("Available mazes:");
            for (int i = 0; i < labyrinthList.size(); i++) {
                System.out.println((i + 1) + ". " + labyrinthList.get(i));
            }
            System.out.print("Select a maze number (1-" + labyrinthList.size() + "): ");
            int choice = scanner.nextInt();

            if (choice < 1 || choice > labyrinthList.size()) {
                System.out.println("Número inválido, selecione um número entre 1 e " + labyrinthList.size());
                return;
            }
            String selectedMaze = labyrinthList.get(choice - 1);
            String user = "Pigrafos";

            long inicioTempoBfs = System.currentTimeMillis();

            solver.graphCreator(user, selectedMaze);

            Map<Integer, List<Integer>> adjacencyList = solver.getGraph().getAdjacencyList();
            System.out.println("Adjacency List: " + adjacencyList);

            System.out.println("Initial: " + solver.getInitialVertex());
            System.out.println("Final: " + solver.getFinalVertex());

            List<Integer> shortestPath = solver.bfs(solver.getInitialVertex(), solver.getFinalVertex());
            System.out.println("Shortest path: " + shortestPath);

            ApiResponse response = solver.validatePath(user, selectedMaze, shortestPath);
            System.out.println(response.isCaminho_valido() ? "Caminho correto!" : "Caminho incorreto!");

            long fimTempoBfs = System.currentTimeMillis();
            System.out.println("Tempo de execução bfs: " + (fimTempoBfs - inicioTempoBfs) + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
