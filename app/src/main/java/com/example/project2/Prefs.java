package com.example.project2;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private static final String PREF_FILE = "randomly_prefs";
    private static final String KEY_USER_ID = "logged_user_id";

    // Save logged-in user id
    public static void saveLoggedInUser(Context context, int userId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_USER_ID, userId).apply();
    }

    // Get logged-in user id, or -1 if none
    public static int getLoggedInUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_USER_ID, -1);
    }

    // Clear the stored user id (logout)
    public static void clearLoggedInUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_USER_ID).apply();
    }
}
