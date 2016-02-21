package com.jekton.mobilelearn.common.network.operator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Request;

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
    private boolean mShutdown;

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
        RequestCompletion wrapper = new RequestCompletion(key, callback);
        HttpRunnable runnable = new HttpRunnable(request, wrapper);

        synchronized (mLock) {
            if (mShutdown) throw new IllegalStateException("The operator has been shut down");
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
            if (mShutdown) throw new IllegalStateException("The operator has been shut down");
            cancelRequestLocked(key);
        }
    }

    @Override
    public void cancelRequests() {
        synchronized (mLock) {
            if (mShutdown) throw new IllegalStateException("The operator has been shut down");
            cancelRequestsLocked();
        }
    }

    private void cancelRequestsLocked() {
        for (Map.Entry<Object, HttpRunnable> entry : mRunnableHashMap.entrySet()) {
            entry.getValue().cancel();
        }
        mRunnableHashMap.clear();
    }

    @Override
    public void shutdown() {
        synchronized (mLock) {
            if (mShutdown) return;
            cancelRequestsLocked();
            mExecutor.shutdown();
            mShutdown = true;
        }
    }



    private class RequestCompletion extends AbstractRequestCompletion {

        private final Object mKey;

        public RequestCompletion(Object key, OnResponseCallback callback) {
            super(callback);
            mKey = key;
        }

        @Override
        protected boolean cleanAndCheck() {
            synchronized (mLock) {
                // only call the origin callback if this runnable is still in the map and the
                // NetworkOperator has not been shut down.
                // otherwise, it must be canceled
                if (mShutdown || mRunnableHashMap.remove(mKey) == null) {
                    return false;
                }
            }
            return true;
        }
    }
}
