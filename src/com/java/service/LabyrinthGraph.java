package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabyrinthGraph {
    private Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
    private Map<Integer, VertexType> vertexTypes = new HashMap<>();

    public LabyrinthGraph() {
        // Constructor
    }

    public void addVertex(int vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(int vertex, int neighbor) {
        adjacencyList.computeIfAbsent(vertex, k -> new ArrayList<>()).add(neighbor);
        // If the graph is undirected, you would also add the reverse edge here
        adjacencyList.computeIfAbsent(neighbor, k -> new ArrayList<>()).add(vertex);
    }

    public List<Integer> getNeighbors(int vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    public void setVertexType(int vertex, VertexType type) {
        vertexTypes.put(vertex, type);
    }

    public VertexType getVertexType(int vertex) {
        return vertexTypes.get(vertex);
    }

    public Map<Integer, VertexType> getVertexTypes() {
        return vertexTypes;
    }

    public Map<Integer, List<Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public void buildGraph(List<LabyrinthResponse> responses) {
        for (LabyrinthResponse response : responses) {
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
