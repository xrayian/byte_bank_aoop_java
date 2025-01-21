package com.kernelcrash.byte_bank.utils;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class HttpClientHelper {
    private final HttpClient httpClient;

    // Constructor
    public HttpClientHelper() {
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Sends a GET request.
     *
     * @param url The URL to send the GET request to.
     * @param headers Optional headers for the request.
     * @return The response body as a String.
     * @throws Exception If an error occurs during the request.
     */
    public String sendGet(String url, Map<String, String> headers) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET();

        // Add headers if provided
        if (headers != null) {
            headers.forEach(builder::header);
        }

        HttpRequest request = builder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        validateResponse(response);

        return response.body();
    }

    /**
     * Sends a POST request with a JSON payload.
     *
     * @param url The URL to send the POST request to.
     * @param jsonBody The JSON payload as a String.
     * @param headers Optional headers for the request.
     * @return The response body as a String.
     */
    public String sendPost(String url, String jsonBody, Map<String, String> headers) throws URISyntaxException {

        ConfigHelper configHelper = new ConfigHelper();

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json");

        // Add headers if provided
        if (headers != null) {
            headers.forEach(builder::header);
        }

        HttpRequest request = builder.build();
        HttpResponse<String> response = null;
        try {
            if(configHelper.debugNetwork) {
                System.out.println("Sending POST request to: " + url);
                System.out.println("Request body: " + jsonBody);
            }
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            validateResponse(response);
        } catch (IOException | InterruptedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText("An error occurred while sending the request. Please try again later.");
            alert.showAndWait();
            throw new RuntimeException(e);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText("Something went wrong. Please try again later.");
            throw new RuntimeException(e);
        }

        return response.body();
    }

    /**
     * Sends a PUT request with a JSON payload.
     *
     * @param url The URL to send the PUT request to.
     * @param jsonBody The JSON payload as a String.
     * @param headers Optional headers for the request.
     * @return The response body as a String.
     * @throws Exception If an error occurs during the request.
     */
    public String sendPut(String url, String jsonBody, Map<String, String> headers) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json");

        // Add headers if provided
        if (headers != null) {
            headers.forEach(builder::header);
        }

        HttpRequest request = builder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        validateResponse(response);

        return response.body();
    }

    /**
     * Sends a DELETE request.
     *
     * @param url The URL to send the DELETE request to.
     * @param headers Optional headers for the request.
     * @return The response body as a String.
     * @throws Exception If an error occurs during the request.
     */
    public String sendDelete(String url, Map<String, String> headers) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .DELETE();

        // Add headers if provided
        if (headers != null) {
            headers.forEach(builder::header);
        }

        HttpRequest request = builder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        validateResponse(response);

        return response.body();
    }

    // Helper method to validate the response
    private void validateResponse(HttpResponse<String> response) throws Exception {
        if (response.statusCode() >= 400) {
            System.err.println("HTTP error: " + response.statusCode() + " - " + response.body());
//            throw new Exception("HTTP error: " + response.statusCode() + " - " + response.body());
        }
    }

    public void createWallet(String walletName, String symbol) {
        walletName = URLEncoder.encode(walletName);
        String apiUrl = ConfigHelper.BACKEND_API_URL + "transactions/open-wallet?uuid=" + StateManager.getInstance().getCurrentUser().getUserId() + "&walletName=" + walletName + "&symbol=" + symbol;
        String jsonBody = "";
        try {
            String res = sendPost(apiUrl, jsonBody, null);
            if (res != null) {
                System.out.println("Wallet created successfully");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
