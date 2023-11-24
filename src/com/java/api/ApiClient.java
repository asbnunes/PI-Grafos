package api;

import java.net.URI;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiClient {

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public ApiClient(String baseUrl) throws NoSuchAlgorithmException, KeyManagementException {
        // SSLContext personalizado
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager[] trustAllCertificates = new TrustManager[] { new InsecureTrustManager() };
        sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

        // Configura HttpClientBuilder para usar o SSLContext personalizado
        httpClient = HttpClients.custom()
                .setSslcontext(sslContext)
                .build();

        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
    }

    private static class InsecureTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }

    private ApiResponse executePostRequest(HttpPost request) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new IOException("Error in request: Status code " + statusCode);
            }
            HttpEntity responseEntity = response.getEntity();
            String jsonResponse = EntityUtils.toString(responseEntity);
            return objectMapper.readValue(jsonResponse, ApiResponse.class);
        }
    }


    public ApiResponse startMaze(String user, String labyrinth) throws IOException {
        String url = baseUrl + "/iniciar";
        HttpPost request = new HttpPost(url);
        String json = "{\"id\":\"" + user + "\",\"labirinto\":\"" + labyrinth + "\"}";

        StringEntity entity = new StringEntity(json, "UTF-8");
        request.setEntity(entity);
        request.setHeader("Content-type", "application/json");
        request.setHeader("Accept", "application/json");

        return executePostRequest(request);
    }


    public ApiResponse move(String user, String labyrinth, int newPosition) throws IOException {
        String url = baseUrl + "/movimentar";
        HttpPost request = new HttpPost(url);
        String json = "{\"id\":\"" + user + "\",\"labirinto\":\"" + labyrinth + "\",\"nova_posicao\":" + newPosition + "}";

        final StringEntity myEntity = new StringEntity(json);

        request.setEntity(myEntity);
        request.setHeader("Content-type", "application/json");
        request.setHeader("Accept", "application/json");

        // Log the request being sent
        System.out.println("Request Body: " + json);

        HttpResponse response = httpClient.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);

        // Log the response
        System.out.println("Response Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);

        if (statusCode == 200) {
            return objectMapper.readValue(responseBody, ApiResponse.class);
        } else {
            // Including the response body in the exception for better debugging
            throw new IOException("Erro na solicitação de movimento: Código de status " + statusCode + " Response Body: " + responseBody);
        }
    }

    public ValidatePathResponse validatePath(ValidatePathRequest request) throws IOException {
        String jsonRequest = objectMapper.writeValueAsString(request);
        HttpPost httpRequest = createRequest(baseUrl + "/valida_caminho", jsonRequest);
        return objectMapper.readValue(sendRequest(httpRequest), ValidatePathResponse.class);
    }

    private ApiResponse postRequest(String path, Object request) throws IOException {
        String jsonRequest = objectMapper.writeValueAsString(request);
        HttpPost httpRequest = createRequest(baseUrl + path, jsonRequest);
        String jsonResponse = sendRequest(httpRequest);
        return objectMapper.readValue(jsonResponse, ApiResponse.class);
    }

    private HttpPost createRequest(String url, String jsonBody) {
        HttpPost request = new HttpPost(url);
        request.setEntity(new StringEntity(jsonBody, "UTF-8"));
        request.setHeader("Content-type", "application/json");
        request.setHeader("Accept", "application/json");
        return request;
    }

    private String sendRequest(HttpPost request) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new IOException("Error in request: Status code " + statusCode);
            }
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        }
    }

    public String getRequest(String path) throws IOException {
        HttpGet request = new HttpGet(baseUrl + path);
        request.setHeader("Accept", "application/json");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new IOException("GET request failed: Status code " + statusCode);
            }
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        }
    }

    // Any other methods from your original ApiClient can be modified similarly.
}
