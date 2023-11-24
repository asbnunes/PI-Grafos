package util;
import api.ApiClient;
import api.ApiResponse;
import api.MoveRequest;
import api.StartMazeRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BuildGraph {
    private ApiClient apiClient;
    private String userId;
    private String mazeId;
    private Map<Integer, Set<Integer>> graph;
    private Set<Integer> visited;

    public BuildGraph(ApiClient apiClient, String userId, String mazeId) {
        this.apiClient = apiClient;
        this.userId = userId;
        this.mazeId = mazeId;
        this.graph = new HashMap<>();
        this.visited = new HashSet<>();
    }

    public Map<Integer, List<Integer>> getAdjacencyList() throws IOException {
        ApiResponse startResponse = apiClient.startMaze(userId, mazeId);
        bfs(startResponse.getPos_atual());
        return createAdjacencyList();
    }

    public void bfs(int startPosition) throws IOException {
        Map<Integer, Integer> distance = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();

        // Initialize for the start position
        distance.put(startPosition, 0);
        visited.add(startPosition);
        queue.add(startPosition);

        while (!queue.isEmpty()) {
            int currentPosition = queue.poll();
            System.out.println("Current Position: " + currentPosition);

            // Get possible movements from the current position
            ApiResponse response = currentPosition == startPosition
                    ? apiClient.startMaze(userId, mazeId)
                    : apiClient.move(userId, mazeId, currentPosition);
            List<Integer> movements = Arrays.stream(response.getMovimentos()).boxed().collect(Collectors.toList());
            System.out.println("Movements from " + currentPosition + ": " + movements);

            for (Integer move : movements) {
                if (!visited.contains(move)) {
                    System.out.println("Adding to queue: " + move);
                    graph.putIfAbsent(currentPosition, new HashSet<>());
                    graph.get(currentPosition).add(move);

                    distance.put(move, distance.get(currentPosition) + 1);
                    visited.add(move);
                    queue.add(move);
                }
            }
        }
    }

    private void processMovements(int currentPosition, List<Integer> movements, Queue<Integer> queue, Map<Integer, Integer> distance, Map<Integer, Integer> parent) {
        graph.putIfAbsent(currentPosition, new HashSet<>());
        for (Integer move : movements) {
            graph.get(currentPosition).add(move);

            if (!distance.containsKey(move)) {
                distance.put(move, distance.get(currentPosition) + 1);
                parent.put(move, currentPosition);
                visited.add(move);
                queue.add(move);
            }
        }
    }

    private Map<Integer, List<Integer>> createAdjacencyList() {
        Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
        for (Map.Entry<Integer, Set<Integer>> entry : graph.entrySet()) {
            adjacencyList.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return adjacencyList;
    }
}


