package com.jekton.mobilelearn.common.util;

import android.util.Log;

/**
 * @author Jekton
 */
public class Logger {

    private static final boolean DEBUG = true;
    private static final boolean ERROR = true;

    public static void d(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable t) {
        if (DEBUG)
            Log.d(tag, msg, t);
    }

    public static void d(String tag, Throwable t) {
        if (DEBUG)
            Log.d(tag, t.getMessage(), t);
    }

    public static void e(String tag, String msg) {
        if (ERROR)
            Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable t) {
        if (ERROR)
            Log.e(tag, msg, t);
    }

    public static void e(String tag, Throwable t) {
        if (ERROR)
            Log.e(tag, t.getMessage(), t);
    }

    private Logger() {
        throw new AssertionError("DON'T instantiate this class");
    }
}
