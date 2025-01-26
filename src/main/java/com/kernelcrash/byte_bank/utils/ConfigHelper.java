package com.kernelcrash.byte_bank.utils;

import com.google.gson.*;
import com.kernelcrash.byte_bank.models.User;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

//make static

public class ConfigHelper {

    //read the server IP and port from a file

    private static String ServerIP;
    private static String ServerPort;

    public boolean debugNetwork = true;

    public ConfigHelper() {
        init();
    }

    public static String getBACKEND_API_URL() {
        return "http://" + ServerIP + ":" + ServerPort + "/api/v1/";
    }

    public static String getBACKEND_WS_URL() {
        return "ws://" + ServerIP + ":" + ServerPort + "/ws";
    }

    public static String getWS_CRYPTO_SOCKET_URL() {
        return "ws://" + ServerIP + ":" + ServerPort + "/crypto-socket";
    }


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
//            try {
            return refreshUserObjectOnline(user.getEmail());
//            } catch (Exception e) {
            //System.err.println("Failed to get latest user object. Using cached object");
//            }
        }

        return user;
    }

    public static void init() {
        //read the server IP and port from a file
        try {
            BufferedReader reader = new BufferedReader(new FileReader("server.config"));
            ServerIP = reader.readLine();
            ServerPort = reader.readLine();
            System.out.println("Server IP: " + ServerIP);
            System.out.println("Server Port: " + ServerPort);
            reader.close();
        } catch (IOException e) {
            System.err.println("Failed to read server config file. Using default values");
            ServerIP = "localhost";
            ServerPort = "8080";
        }
    }

    public static User refreshUserObjectOnline(String email) {
        /// reached here print stack trace
        //todo print why multiple calls to this method
        ///
        HttpClientHelper httpClientHelper = new HttpClientHelper();
        String urlParamEmail = null;
        try {
            urlParamEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Failed to encode email");
            return null;
        }
        String apiUrl = ConfigHelper.getBACKEND_API_URL() + "auth/refresh-user?email=" + urlParamEmail;
        try {
            String response = httpClientHelper.sendPost(apiUrl, "", null);
            if (response != null) {
                Gson gson = GsonWithLocalDateTimeImpl();
                User user = gson.fromJson(response, User.class);
                System.out.println("User refreshed: " + user.getUsername());
                return user;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to refresh user object");
        }
        return null;
    }

    public static Gson GsonWithLocalDateTimeImpl() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>)
                        (json, type, context) -> LocalDateTime.parse(json.getAsString()))
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)
                        (src, type, context) -> new JsonPrimitive(src.toString()))
                .create();
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

    public static ConfigHelper getInstance() {
        return new ConfigHelper();
    }
}
