package com.jekton.mobilelearn.common.network;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import okhttp3.Request;

/**
 * @author Jekton
 */
class SingleRequestOperator implements NetworkOperator {

    private final Executor mExecutor;
    private volatile HttpRunnable mRunnable;

    public SingleRequestOperator(Executor executor) {
        mExecutor = executor;
    }

    @Override
    public void executeRequest(@NonNull Request request,
                               @NonNull OnResponseCallback callback) {
        // cancel the previous request since we can just handle a single request at a time
        if (mRunnable != null) {
            mRunnable.cancel();
        }

        mRunnable = new HttpRunnable(request, callback);
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
        mRunnable.cancel();
        mRunnable = null;
    }
}
