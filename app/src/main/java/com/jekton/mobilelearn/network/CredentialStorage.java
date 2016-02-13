package com.jekton.mobilelearn.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.jekton.mobilelearn.MyApplication;

/**
 * @author Jekton
 */
public class CredentialStorage {

    private static final String SP_CREDENTIAL_NAME = "SP_CREDENTIAL_NAME";
    private static final String CREDENTIAL_KEY_EMAIL = "CREDENTIAL_KEY_EMAIL";
    private static final String CREDENTIAL_KEY_PASSWORD = "CREDENTIAL_KEY_PASSWORD";

    private static SharedPreferences sPreferences;

    static {
        sPreferences = MyApplication.getInstance()
                .getSharedPreferences(SP_CREDENTIAL_NAME,
                                      Context.MODE_PRIVATE);
    }


    public static void storeCredential(String email, String password) {
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putString(CREDENTIAL_KEY_EMAIL, email);
        editor.putString(CREDENTIAL_KEY_PASSWORD, password);
        editor.apply();
    }

    /**
     * @return a String[] of email and password
     */
    public static String[] getCredential() {
        String email = sPreferences.getString(CREDENTIAL_KEY_EMAIL, "ljtong64@outlook.com");
        String password = sPreferences.getString(CREDENTIAL_KEY_PASSWORD, "964698758");

        return new String[] {
                email,
                password
        };
    }



    private CredentialStorage() {
    }
}
