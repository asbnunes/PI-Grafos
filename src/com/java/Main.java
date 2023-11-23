import api.*;
import com.google.gson.Gson;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        try {
            ApiClient apiClient = new ApiClient("http://localhost:63291");

            // Fetch the list of mazes from the API
            String mazesJson = apiClient.getRequest("/labirintos");

            // Assuming the API returns a JSON array of strings
            String[] mazes = new Gson().fromJson(mazesJson, String[].class);

            // Print the list of mazes
            if (mazes != null) {
                System.out.println("List of mazes:");
                Arrays.stream(mazes).forEach(System.out::println);
            } else {
                System.out.println("No mazes found or empty response.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve the list of mazes: " + e.getMessage());
        }
    }
}
