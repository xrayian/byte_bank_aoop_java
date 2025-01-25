package com.kernelcrash.byte_bank.utils;

import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.models.Wallet;
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

    public boolean convertCurrency(Wallet fromWallet, Wallet toWallet, double fromAmount) {
        // Encode the wallet names
        String apiUrl = ConfigHelper.BACKEND_API_URL + "transactions/convert-currency-between-wallets?uuid=" + StateManager.getInstance().getCurrentUser().getUserId() + "&amount=" + fromAmount + "&fromWalletId=" + fromWallet.getWalletId() + "&toWalletId=" + toWallet.getWalletId();
        String jsonBody = "";
        Map<String, String> headers = Map.of("Accept", "application/json");
        try {
            String res = sendPost(apiUrl, jsonBody, headers);
            if (res != null) {
                System.out.println("Currency converted successfully");
                //refresh the wallets

                return true;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean transferCurrency(Wallet fromWallet, String toUUID, double fromAmount) {
        // Encode the wallet names
        //http://localhost:8080/api/v1/transactions/send-currency-between-users?senderUUID=52386b6a-f49f-4e56-a889-b3a6f8f8236f&receiverUUID=676526bb-27ee-4618-a1f7-08f59b4a3358&amount=5&currency=USD&senderWalletAddress=1
        String apiUrl = ConfigHelper.BACKEND_API_URL + "transactions/send-currency-between-users?senderUUID=" + MainApplication.stateManager.getCurrentUser().getUserId() + "&receiverUUID=" + toUUID + "&amount=" + fromAmount + "&currency=" + fromWallet.getCryptoType() + "&senderWalletAddress=" + fromWallet.getWalletId();
        String jsonBody = "";
        Map<String, String> headers = Map.of("Accept", "application/json");
        try {
            String res = sendPost(apiUrl, jsonBody, headers);
            if (res != null) {
                System.out.println("Currency transferred successfully");
                //refresh the wallets

                return true;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Sends a GET request.
     *
     * @param url     The URL to send the GET request to.
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
     * @param url      The URL to send the POST request to.
     * @param jsonBody The JSON payload as a String.
     * @param headers  Optional headers for the request.
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
            if (configHelper.debugNetwork) {
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
     * @param url      The URL to send the PUT request to.
     * @param jsonBody The JSON payload as a String.
     * @param headers  Optional headers for the request.
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
     * @param url     The URL to send the DELETE request to.
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
        Map<String, String> headers = Map.of("Accept", "application/json");
        try {
            String res = sendPost(apiUrl, jsonBody, headers);
            if (res != null) {
                System.out.println("Wallet created successfully");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
