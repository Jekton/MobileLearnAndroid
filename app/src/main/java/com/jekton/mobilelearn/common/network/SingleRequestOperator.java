package com.jekton.mobilelearn.common.network;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Synchronized by the instance itself
 *
 * @author Jekton
 */
class SingleRequestOperator implements NetworkOperator {

    private final Executor mExecutor;
    private HttpRunnable mRunnable;

    public SingleRequestOperator(Executor executor) {
        mExecutor = executor;
    }

    @Override
    public void executeRequest(@NonNull Request request,
                               @NonNull OnResponseCallback callback) {

        mRunnable = new HttpRunnable(request,
                                     new OnResponseCallbackWrapper(callback));

        synchronized (this) {
            // cancel the previous request since we can just handle a single request at a time
            if (mRunnable != null) {
                mRunnable.cancel();
            }
            mExecutor.execute(mRunnable);
        }
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
        synchronized (this) {
            if (mRunnable != null) {
                mRunnable.cancel();
                mRunnable = null;
            }
        }
    }


    private class OnResponseCallbackWrapper implements OnResponseCallback {

        private final OnResponseCallback mOriginCallback;

        public OnResponseCallbackWrapper(OnResponseCallback callback) {
            mOriginCallback = callback;
        }

        @Override
        public void onResponseSuccess(Response response) {
            synchronized (SingleRequestOperator.this) {
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
