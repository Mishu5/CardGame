package com.example.kierki.client;

/**
 * Global name of player for all classes
 */

public class UserName {

    private static String userName;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserName.userName = userName;
    }
}
