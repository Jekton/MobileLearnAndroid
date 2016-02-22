package com.jekton.mobilelearn.common.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.jekton.mobilelearn.MyApplication;

/**
 * @author Jekton
 */
public class CredentialStorage {

    private static final String SP_CREDENTIAL_NAME = "SP_CREDENTIAL_NAME";
    private static final String KEY_EMAIL = "KEY_EMAIL";
    private static final String KEY_PASSWORD = "KEY_PASSWORD";
    private static final String KEY_LOGGED_IN = "KEY_LOGGED_IN";



    public static void storeCredential(String email, String password) {
        SharedPreferences preferences = MyApplication.getInstance()
                .getSharedPreferences(SP_CREDENTIAL_NAME,
                                      Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.apply();
    }

    /**
     * @return a String[] of email and password
     */
    public static String[] getCredential() {
        SharedPreferences preferences = MyApplication.getInstance()
                .getSharedPreferences(SP_CREDENTIAL_NAME,
                                      Context.MODE_PRIVATE);

        String email = preferences.getString(KEY_EMAIL, "");
        String password = preferences.getString(KEY_PASSWORD, "");

        return new String[] {
                email,
                password
        };
    }


    public static boolean isLogin() {
        SharedPreferences preferences = MyApplication.getInstance()
                .getSharedPreferences(SP_CREDENTIAL_NAME,
                                      Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_LOGGED_IN, false);
    }


    private CredentialStorage() {
    }
}
