package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ApiClient {

    private final HttpClient httpClient;
    private final String baseUrl;
    private final Gson gson;

    public ApiClient(String baseUrl) {
        if (!baseUrl.startsWith("http://")) {
            baseUrl = "http://" + baseUrl;
        }
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public ApiResponse startMaze(StartMazeRequest request) throws IOException, InterruptedException {
        return postRequest("/iniciar", request);
    }

    public ApiResponse move(MoveRequest request) throws IOException, InterruptedException {
        return postRequest("/movimentar", request);
    }

    public ValidatePathResponse validatePath(ValidatePathRequest request) throws IOException, InterruptedException {
        String jsonRequest = gson.toJson(request);
        HttpRequest httpRequest = createRequest(baseUrl + "/valida_caminho", jsonRequest, "POST");
        String jsonResponse = sendRequest(httpRequest);
        return gson.fromJson(jsonResponse, ValidatePathResponse.class);
    }

    private ApiResponse postRequest(String path, Object request) throws IOException, InterruptedException {
        String jsonRequest = gson.toJson(request);
        HttpRequest httpRequest = createRequest(baseUrl + path, jsonRequest, "POST");
        String jsonResponse = sendRequest(httpRequest);
        try {
            return gson.fromJson(jsonResponse, ApiResponse.class); // Deserialize the JSON response
        } catch (JsonSyntaxException e) {
            System.err.println("Failed to parse JSON response: " + jsonResponse);
            throw e; // Rethrow the exception to handle it appropriately
        }
    }

    private HttpRequest createRequest(String url, String jsonBody, String method) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json");

        if ("POST".equals(method)) {
            requestBuilder.POST(BodyPublishers.ofString(jsonBody));
        }

        return requestBuilder.build();
    }

    private String sendRequest(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        System.out.println("Raw response: " + responseBody); // Log the raw response
        return responseBody;
    }

    public String getRequest(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Log the status code and the full response for debugging
        System.out.println("Response status code: " + response.statusCode());
        System.out.println("Raw response: " + response.body());

        return response.body();
    }
}