package service;

import api.ApiClient;
import api.ApiResponse;

import java.io.IOException;
import java.util.*;

public class LabyrinthSolver {
    private final ApiClient client;
    private final LabyrinthGraph graph;
    private final Stack<ApiResponse> path;
    private final Set<Integer> visited;

    public List<String> getMazes() throws IOException {
        return client.getMazes();
    }

    public LabyrinthSolver(ApiClient client) {
        this.client = client;
        this.graph = new LabyrinthGraph();
        this.visited = new HashSet<>();
        this.path = new Stack<>();
    }

    public LabyrinthGraph graphCreator(String user, String labyrinth) throws IOException {
        ApiResponse starting = client.startMaze(user, labyrinth);

        navigating(user, labyrinth, starting);
        return graph;
    }

    private void navigating(String user, String labirinth, ApiResponse currentPosition) throws IOException {
        visited.add(currentPosition.getPos_atual());
        graph.addVertex(currentPosition.getPos_atual());

        if (currentPosition.isInicio()) {
            graph.setVertexType(currentPosition.getPos_atual(), VertexType.INITIAL);
        } else if (currentPosition.isFim()) {
            graph.setVertexType(currentPosition.getPos_atual(), VertexType.FINAL);
        } else {
            graph.setVertexType(currentPosition.getPos_atual(), VertexType.COMMON);
        }

        graph.buildGraph(List.of());
        path.push(currentPosition);

        for (int newPosition : currentPosition.getMovimentos()) {
            if (!visited.contains(newPosition)) {
                ApiResponse apiResponse = client.move(user, labirinth, newPosition);
                navigating(user, labirinth, apiResponse);
            }
        }

        path.pop();
        if (path.size() - 1 >= 0) {
            client.move(user, labirinth, path.get(path.size() - 1).getPos_atual());
        }

    }

    public List<Integer> bfs(int source, int destination) {
        BfsSearch graphSearch = new BfsSearch();
        return graphSearch.bfs(graph, source, destination);
    }

    public ApiResponse validatePath(String user, String labirinth, List<Integer> moves) throws IOException {
        return client.validatePath(user, labirinth, moves);
    }
}

