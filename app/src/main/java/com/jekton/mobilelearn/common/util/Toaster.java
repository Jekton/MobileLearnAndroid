package com.jekton.mobilelearn.common.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * @author Jekton
 */
public class Toaster {

    public static void showShort(Context context, @StringRes int msgId) {
        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }



    private Toaster() {
        throw new AssertionError("DON'T instantiate this class");
    }
}
