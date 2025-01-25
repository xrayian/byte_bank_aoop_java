package com.kernelcrash.byte_bank.utils;

import com.kernelcrash.byte_bank.models.User;

public class StateManager {
    private static StateManager instance;
    //private boolean isDarkMode;
    private User currentUser;

    private StateManager() {
    }

    public static StateManager getInstance() {
        if (instance == null) {
            instance = new StateManager();
        }
        if (ConfigHelper.loadLoggedInUserObject() != null) {
            instance.currentUser = ConfigHelper.loadLoggedInUserObject();
        }
        return instance;
    }

    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public void refreshUser() {
        currentUser = ConfigHelper.refreshUserObjectOnline(currentUser.getEmail());
    }


//    public boolean isDarkMode() {
//        return isDarkMode;
//    }
//
//    public void setDarkMode(boolean darkMode) {
//        isDarkMode = darkMode;
//    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void logout() {
        currentUser = null;
        if (ConfigHelper.deleteLoggedInUserObject()) {
            System.out.println("User logged out");
        } else {
            System.out.println("Error logging out user");
        }
    }
}
