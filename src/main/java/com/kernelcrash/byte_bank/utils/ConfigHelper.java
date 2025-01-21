package com.kernelcrash.byte_bank.utils;

import com.google.gson.Gson;
import com.kernelcrash.byte_bank.models.User;

import java.io.*;
import java.net.URISyntaxException;

public class ConfigHelper {
    public static final String BACKEND_API_URL = "http://localhost:8080/api/v1/";
    public static final String BACKEND_WS_URL = "ws://localhost:8080/";
    public static final String WS_CRYPTO_SOCKET_URL = "ws://localhost:8080/crypto-socket";
    public boolean debugNetwork = true;

    public static User loadLoggedInUserObject() {
        User user = null;
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("user.ser");
            ois = new ObjectInputStream(fis);
            user = (User) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            System.out.println("No user logged in");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (user != null) {
            System.out.println("User is logged in: " + user.getUsername());
            try {
                return refreshUserObject(user.getEmail());
            } catch (Exception e) {
                System.err.println("Failed to get latest user object. Using cached object");
            }
        }
        return user;
    }

    private static User refreshUserObject(String email) {
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        String apiUrl = ConfigHelper.BACKEND_API_URL + "auth/refresh-user?email=" + email;
        try {
            String response = httpClientHelper.sendPost(apiUrl, null, null);
            if (response != null) {
                Gson gson = new Gson();
                User user = gson.fromJson(response, User.class);
                System.out.println("User refreshed: " + user.getUsername());
                return user;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to refresh user object");
        }
        return null;
    }

    public static boolean storeLoggedInUserObject() {

        StateManager stateManager = StateManager.getInstance();
        User currentUser = stateManager.getCurrentUser();

        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("user.ser");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(currentUser);
            oos.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean deleteLoggedInUserObject() {
        File file = new File("user.ser");
        return file.delete();
    }

}
