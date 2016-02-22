package com.jekton.mobilelearn.common.util;

import android.app.Activity;
import android.content.Intent;

import com.jekton.mobilelearn.course.MainActivity;

/**
 * @author Jekton
 */
public class NavigationUtil {

    public static void gotoMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    private NavigationUtil() {
    }
}
