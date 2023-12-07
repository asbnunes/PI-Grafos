package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToGraph {
    private Map<Integer, List<Integer>> adjacencyList = new HashMap<>();

    public void addVertex(int vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(int vertex, int neighbor) {
        adjacencyList.computeIfAbsent(vertex, k -> new ArrayList<>()).add(neighbor);
        adjacencyList.computeIfAbsent(neighbor, k -> new ArrayList<>()).add(vertex);
    }

    public List<Integer> getNeighbors(int vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    public Map<Integer, List<Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public void buildGraph(List<MazeResponse> responses) {
        for (MazeResponse response : responses) {
            int currentPosition = response.getActualPosition();
            List<Integer> possibleMoves = response.getMovimentos();

            if (!adjacencyList.containsKey(currentPosition)) {
                addVertex(currentPosition);
            }

            for (int newPosition : possibleMoves) {
                if (!adjacencyList.containsKey(newPosition)) {
                    addVertex(newPosition);
                }
                if (!adjacencyList.get(currentPosition).contains(newPosition)) {
                    addEdge(currentPosition, newPosition);
                }
            }
        }
    }
}
