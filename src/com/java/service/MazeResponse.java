package service;

import api.ApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MazeResponse {
    @JsonProperty("pos_atual")
    private int actualPosition;
    @JsonProperty("movimentos")
    private List<Integer> possibleMoves;

    public int getActualPosition() {
        return actualPosition;
    }

    public List<Integer> getMovimentos() {
        return possibleMoves;
    }

    public static MazeResponse fromApiResponse(ApiResponse apiResponse) {
        MazeResponse response = new MazeResponse();
        response.actualPosition = apiResponse.getPos_atual();
        response.possibleMoves = Arrays.stream(apiResponse.getMovimentos())
                .boxed()
                .collect(Collectors.toList());
        return response;
    }
}
