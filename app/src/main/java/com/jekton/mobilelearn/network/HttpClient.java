package com.jekton.mobilelearn.network;

import android.util.Log;

import com.jekton.mobilelearn.MyApplication;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;

/**
 * @author Jekton
 */
public class HttpClient {

    private static final String LOG_TAG = HttpClient.class.getSimpleName();

    private static class Singleton {
        static OkHttpClient sClient;

        static {
            Log.e(LOG_TAG, MyApplication.getInstance() + "");
            CookieHandler cookieHandler = new CookieManager(
                    new PersistentCookieStore(MyApplication.getInstance()), CookiePolicy.ACCEPT_ALL);

            // init OkHttpClient
            sClient = new OkHttpClient.Builder()
                    .cookieJar(new JavaNetCookieJar(cookieHandler))
                    .build();
        }
    }


    private HttpClient() {
        throw new AssertionError("Cannot instantiate this class!");
    }


    public static OkHttpClient getInstance() {
        return Singleton.sClient;
    }
}
