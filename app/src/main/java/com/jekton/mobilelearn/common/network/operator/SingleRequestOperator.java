package com.jekton.mobilelearn.common.network.operator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Request;

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
    private boolean mShutdown;

    public SingleRequestOperator() {
        mExecutor = Executors.newSingleThreadExecutor();
        mLock = new Object();
    }

    @Override
    public void executeRequest(@NonNull Request request,
                               @NonNull OnResponseCallback callback) {

        mRunnable = new HttpRunnable(request,
                                     new RequestCompletion(callback));

        synchronized (mLock) {
            // it's a race condition if checking out of the critical session by making
            // mShutdown volatile
            if (mShutdown) {
                throw new IllegalStateException("Couldn't execute request while it's shut down");
            }
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
    public void cancelRequest(@Nullable Object key) {
        // there is just a single request
        cancelRequests();
    }

    @Override
    public void cancelRequests() {

        synchronized (mLock) {
            if (mShutdown) {
                throw new IllegalStateException("Couldn't cancel a request in a shut down instance");
            }
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

        synchronized (mLock) {
            if (mShutdown) return;

            cancelRequestsLocked();
            mExecutor.shutdown();
            mShutdown = true;
        }
    }



    private class RequestCompletion extends AbstractRequestCompletion {

        public RequestCompletion(OnResponseCallback callback) {
            super(callback);
        }

        @Override
        protected boolean cleanAndCheck() {
            synchronized (mLock) {
                if (mRunnable == null) {
                    return false;
                } else {
                    mRunnable = null;
                    return true;
                }
            }
        }
    }

}
