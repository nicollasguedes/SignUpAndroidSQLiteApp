package com.example.signupsqliteapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "MyAppPreferences";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ID = "user_id";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void saveUserSession(String email, int userId) {
        editor.putString(KEY_EMAIL, email);
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getUserEmail() {
        return preferences.getString(KEY_EMAIL, "");
    }

    public int getUserId() {
        return preferences.getInt(KEY_USER_ID, -1);
    }

    public boolean isLoggedIn() {
        return !getUserEmail().isEmpty();
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }


}
