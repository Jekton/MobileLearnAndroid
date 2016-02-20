package com.jekton.mobilelearn.common.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private final ExecutorService mExecutor;
    private final Map<Object, HttpRunnable> mRunnableHashMap;
    private final Random mRandom;
    private final Object mLock;
    private volatile boolean shutdown;

    public MultiRequestOperator(Map<Object, HttpRunnable> map) {
        mExecutor = Executors.newFixedThreadPool(5);
        mRunnableHashMap = map;
        mRandom = new Random();
        mLock = new Object();
    }


    /**
     * Must be called with the lock
     * @return A key that doesn't used
     */
    private int generateUnusedKey() {
        while (true) {
            int key = mRandom.nextInt();
            if (!mRunnableHashMap.containsKey(key)) {
                return key;
            }
        }
    }

    /**
     * @param key null if we need to generate a key for it
     * @param request request to be executed
     * @param callback callback of the request
     */
    private void execute(@Nullable Object key,
                         @NonNull Request request,
                         @NonNull OnResponseCallback callback) {
        OnResponseCallbackWrapper wrapper = new OnResponseCallbackWrapper(key, callback);
        HttpRunnable runnable = new HttpRunnable(request, wrapper);

        synchronized (mLock) {
            if (key != null) {
                cancelRequestLocked(key);
            } else {
                key = generateUnusedKey();
            }
            mRunnableHashMap.put(key, runnable);
        }
        // Since the executor might have limited resources, the execute() method might be blocked,
        // we won't to execute this line in the critical session.
        // If the runnable is canceled before the following line is executed, the runnable is
        // guaranteed not to execute the request, therefore, it's safe to call it out of the critical
        // session.
        mExecutor.execute(runnable);
    }

    @Override
    public void executeRequest(@NonNull Request request,
                               @NonNull OnResponseCallback callback) {
        execute(null, request, callback);
    }

    @Override
    public void executeRequest(@NonNull Object key,
                               @NonNull Request request,
                               @NonNull OnResponseCallback callback) {
        execute(key, request, callback);
    }


    private void cancelRequestLocked(@NonNull Object key) {
        if (mRunnableHashMap.containsKey(key)) {
            mRunnableHashMap.get(key).cancel();
            mRunnableHashMap.remove(key);
        }
    }

    @Override
    public void cancelRequest(@NonNull Object key) {
        synchronized (mLock) {
            cancelRequestLocked(key);
        }
    }

    @Override
    public void cancelRequests() {
        synchronized (mLock) {
            for (Map.Entry<Object, HttpRunnable> entry : mRunnableHashMap.entrySet()) {
                entry.getValue().cancel();
            }
            mRunnableHashMap.clear();
        }
    }

    @Override
    public void shutdown() {
        // TODO: 2/19/2016
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
            synchronized (mLock) {
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
