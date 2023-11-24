import api.ApiClient;
import api.ApiResponse;
import com.google.gson.Gson;
import service.BfsSearch;
import service.LabyrinthGraph;
import service.LabyrinthSolver;
import service.VertexType;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    public static void main(String[] args) {
        LabyrinthSolver solver = null;
        try {
            solver = new LabyrinthSolver(new ApiClient("https://gtm.delary.dev"));
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return;
        }

        long inicioTempoBfs = System.currentTimeMillis();
        try {
            List<String> labyrinthList = solver.getMazes();
            System.out.println("Available mazes:");
            for (int i = 0; i < labyrinthList.size(); i++) {
                System.out.println((i + 1) + ". " + labyrinthList.get(i));
            }
            System.out.print("Select a maze number (1-" + labyrinthList.size() + "): ");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            // Validate user input
            if (choice < 1 || choice > labyrinthList.size()) {
                System.out.println("Invalid selection. Please enter a number between 1 and " + labyrinthList.size());
                return;
            }
            String selectedMaze = labyrinthList.get(choice - 1);
            String user = "Pigrafos";

            Map<Integer, VertexType> vertexTypes = solver.graphCreator(user, selectedMaze).getVertexTypes();
            Map<Integer, List<Integer>> adjacencyList = solver.graphCreator(user, selectedMaze).getAdjacencyList();
            AtomicReference<Integer> destination = new AtomicReference<>();
            AtomicReference<Integer> source = new AtomicReference<>();
            System.out.println(vertexTypes);
            System.out.println(adjacencyList);

            vertexTypes.forEach((k, v) -> {
                if (v.equals(VertexType.valueOf("INITIAL"))) {
                    source.set(k);
                } else if (v.equals(VertexType.valueOf("FINAL"))) {
                    destination.set(k);
                }
            });

            System.out.println(solver.bfs(source.get(), destination.get()));
            System.out.println(solver.validatePath(user, selectedMaze, solver.bfs(source.get(), destination.get())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        long fimTempoBfs = System.currentTimeMillis();
        long execucaoTempoBfs = fimTempoBfs - inicioTempoBfs;

        System.out.println("Tempo de execução bfs: " + execucaoTempoBfs + " ms");
    }
}
