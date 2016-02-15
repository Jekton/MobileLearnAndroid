package com.jekton.mobilelearn.common.network;

import android.util.Log;

import com.jekton.mobilelearn.MyApplication;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.Authenticator;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * @author Jekton
 */
public class HttpClient {

    private static final String LOG_TAG = HttpClient.class.getSimpleName();

    private static class Singleton {
        static OkHttpClient sClient;

        static {
            CookieHandler cookieHandler = new CookieManager(
                    new PersistentCookieStore(MyApplication.getInstance()), CookiePolicy.ACCEPT_ALL);

            // init OkHttpClient
            sClient = new OkHttpClient.Builder()
                    .cookieJar(new JavaNetCookieJar(cookieHandler))
                    .authenticator(new Authenticator() {

                        @Override
                        public Request authenticate(Route route, Response response) throws IOException {
                            Log.d(LOG_TAG, "auto re-login");
                            // manually re-login if encounter an authentication problem

                            // It must be safe to use sClient here since the following lines wont't
                            // be called before sClient constructed.
                            Response loginResponse = sClient.newCall(HttpUtils.makeLoginRequest())
                                    .execute();

                            if (loginResponse.isSuccessful())
                                return response.request().newBuilder().build();
                            else
                                return null;
                        }
                    })
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
