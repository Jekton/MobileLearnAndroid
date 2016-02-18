package com.jekton.mobilelearn.common.network;

import android.support.annotation.NonNull;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;

import okhttp3.Request;
import okhttp3.Response;

/**
 * {@link NetworkOperator} that support multiple request to be executed at a time.
 *
 * The execution of this class is synchronized by the instance itself.
 *
 * @author Jekton
 */
class MultiRequestOperator implements NetworkOperator {

    private final Executor mExecutor;
    private final Map<Object, HttpRunnable> mRunnableHashMap;
    private final Random mRandom;

    public MultiRequestOperator(Executor executor, Map<Object, HttpRunnable> map) {
        mExecutor = executor;
        mRunnableHashMap = map;
        mRandom = new Random();
    }


    private int generateUnusedKey() {
        while (true) {
            int key = mRandom.nextInt();
            if (!mRunnableHashMap.containsKey(key)) {
                return key;
            }
        }
    }

    private void execute(Object key,
                         Request request,
                         OnResponseCallback callback) {
        cancelRequest(key);
        OnResponseCallbackWrapper wrapper = new OnResponseCallbackWrapper(key, callback);
        HttpRunnable runnable = new HttpRunnable(request, wrapper);
        mRunnableHashMap.put(key, runnable);
        mExecutor.execute(runnable);
    }

    @Override
    public void executeRequest(@NonNull Request request,
                               @NonNull OnResponseCallback callback) {
        synchronized (this) {
            execute(generateUnusedKey(), request, callback);
        }
    }

    @Override
    public void executeRequest(@NonNull Object key,
                               @NonNull Request request,
                               @NonNull OnResponseCallback callback) {
        synchronized (this) {
            execute(key, request, callback);
        }
    }

    @Override
    public void cancelRequest(@NonNull Object key) {
        synchronized (this) {
            if (mRunnableHashMap.containsKey(key)) {
                mRunnableHashMap.get(key).cancel();
                mRunnableHashMap.remove(key);
            }
        }
    }

    @Override
    public void cancelRequests() {
        synchronized (this) {
            for (Map.Entry<Object, HttpRunnable> entry : mRunnableHashMap.entrySet()) {
                entry.getValue().cancel();
            }
            mRunnableHashMap.clear();
        }
    }



    private class OnResponseCallbackWrapper implements OnResponseCallback {

        private final Object mKey;
        private final OnResponseCallback mOriginCallback;

        public OnResponseCallbackWrapper(Object key, OnResponseCallback callback) {
            mKey = key;
            mOriginCallback = callback;
        }

        @Override
        public void onResponseSuccess(Response response) {
            synchronized (MultiRequestOperator.class) {
                // only call the origin callback if this runnable is still in the map,
                // otherwise, it must be canceled
                if (mRunnableHashMap.remove(mKey) != null) {
                    mOriginCallback.onResponseSuccess(response);
                }
            }
        }

        @Override
        public void onNetworkFail() {
            mOriginCallback.onNetworkFail();
        }

        @Override
        public void onResponseFail(Response response) {
            mOriginCallback.onResponseFail(response);
        }
    }
}
