package com.kernelcrash.byte_bank.utils;

import com.kernelcrash.byte_bank.models.User;

import java.io.*;

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
        return user;
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
