package com.jekton.mobilelearn;

import android.app.Application;

/**
 * @author Jekton
 */
public class MyApplication extends Application {

    private static final String LOG_TAG = MyApplication.class.getSimpleName();

    private static volatile Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;
    }

    public static Application getInstance() {
        return sApplication;
    }
}
