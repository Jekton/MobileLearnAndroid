package com.jekton.mobilelearn.common.network;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple implementation of {@link NetworkOperator} which can handle a request at the same time.
 *
 * This class is thread-safe.
 *
 * All the method of this class will throw {@link IllegalStateException} if it has been shut down.
 *
 * @author Jekton
 */
class SingleRequestOperator implements NetworkOperator {

    private final ExecutorService mExecutor;
    private final Object mLock;

    private HttpRunnable mRunnable;
    private volatile boolean mShutdown;

    public SingleRequestOperator() {
        mExecutor = Executors.newSingleThreadExecutor();
        mLock = new Object();
    }

    @Override
    public void executeRequest(@NonNull Request request,
                               @NonNull OnResponseCallback callback) {
        if (mShutdown) {
            throw new IllegalStateException("Couldn't execute request while it's shut down");
        }

        mRunnable = new HttpRunnable(request,
                                     new OnResponseCallbackWrapper(callback));

        synchronized (mLock) {
            // cancel the previous request since we can just handle a single request at a time
            if (mRunnable != null) {
                mRunnable.cancel();
            }
        }
        /**
         * {@link HttpRunnable} guarantees that the request will not be process if it's canceled
         */
        mExecutor.execute(mRunnable);
    }

    @Override
    public void executeRequest(@NonNull Object key,
                               @NonNull Request request,
                               @NonNull OnResponseCallback callback) {
        // just ignore the key since we don't support it
        executeRequest(request, callback);
    }

    @Override
    public void cancelRequest(@NonNull Object key) {
        // there is just a single request
        cancelRequests();
    }

    @Override
    public void cancelRequests() {
        if (mShutdown) {
            throw new IllegalStateException("Couldn't cancel a request in a shut down instance");
        }

        synchronized (mLock) {
            cancelRequestsLocked();
        }
    }

    private void cancelRequestsLocked() {
        if (mRunnable != null) {
            mRunnable.cancel();
            mRunnable = null;
        }
    }


    /**
     * Shut down the {@link NetworkOperator}.
     *
     * This method will also cancel the executing request if it exists.
     */
    @Override
    public void shutdown() {
        if (mShutdown) {
            throw new IllegalStateException("The instance is already shutdown.");
        }

        synchronized (mLock) {
            cancelRequests();
            mExecutor.shutdown();
            mShutdown = true;
        }
    }

    private class OnResponseCallbackWrapper implements OnResponseCallback {

        private final OnResponseCallback mOriginCallback;

        public OnResponseCallbackWrapper(OnResponseCallback callback) {
            mOriginCallback = callback;
        }

        @Override
        public void onResponseSuccess(Response response) {
            synchronized (mLock) {
                mRunnable = null;
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
