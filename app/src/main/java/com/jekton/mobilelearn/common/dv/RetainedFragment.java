package com.jekton.mobilelearn.common.dv;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;

import java.util.HashMap;

/**
 * @author Jekton
 */
public class RetainedFragment extends Fragment {

    private static final String LOG_TAG = RetainedFragment.class.getSimpleName();

    private HashMap<String, Object> mData = new HashMap<>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "RetainedFragment created");
    }

    <T> T getRetainedObject(String key) {
        return (T) mData.get(key);
    }

    void putRetainedObject(String key, Object obj) {
        mData.put(key, obj);
    }

    public void putRetainedObject(Object object) {
        mData.put(object.getClass().getName(), object);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "RetainedFragment destroyed");
    }
}
