package service;

import api.ApiClient;
import api.ApiResponse;
import lombok.Getter;

import java.io.IOException;
import java.util.*;

@Getter
public class MazeSolver {
    private final ApiClient client;
    private final ToGraph graph;
    private final Stack<ApiResponse> path;
    private final Set<Integer> visited;
    private int initialVertex = -1;
    private int finalVertex = -1;

    public List<String> getMazes() throws IOException {
        return client.getMazes();
    }

    public MazeSolver(ApiClient client) {
        this.client = client;
        this.graph = new ToGraph();
        this.visited = new HashSet<>();
        this.path = new Stack<>();
    }

    public ToGraph graphCreator(String user, String labyrinth) throws IOException {
        ApiResponse starting = client.startMaze(user, labyrinth);

        navigating(user, labyrinth, starting);
        return graph;
    }

    private void navigating(String user, String labyrinth, ApiResponse currentPosition) throws IOException {
        visited.add(currentPosition.getPos_atual());
        graph.addVertex(currentPosition.getPos_atual());

        if (currentPosition.isInicio()) {
            initialVertex = currentPosition.getPos_atual();
        } else if (currentPosition.isFim()) {
            finalVertex = currentPosition.getPos_atual();
        }

        List<MazeResponse> labyrinthResponses = new ArrayList<>();

        path.push(currentPosition);

        for (int newPosition : currentPosition.getMovimentos()) {
            if (!visited.contains(newPosition)) {
                ApiResponse apiResponse = client.move(user, labyrinth, newPosition);

                MazeResponse labyrinthResponse = MazeResponse.fromApiResponse(apiResponse);
                labyrinthResponses.add(labyrinthResponse);

                navigating(user, labyrinth, apiResponse);
            }
        }

        path.pop();
        if (path.size() - 1 >= 0) {
            client.move(user, labyrinth, path.get(path.size() - 1).getPos_atual());
        }

        graph.buildGraph(labyrinthResponses);
    }

    public List<Integer> bfs(int source, int destination) {
        Bfs graphSearch = new Bfs();
        return graphSearch.bfs(graph, source, destination);
    }

    public ApiResponse validatePath(String user, String labirinth, List<Integer> moves) throws IOException {
        return client.validatePath(user, labirinth, moves);
    }

}

