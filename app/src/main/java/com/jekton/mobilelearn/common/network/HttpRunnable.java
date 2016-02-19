package com.jekton.mobilelearn.common.network;

import com.jekton.mobilelearn.common.util.Logger;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Jekton
 */
class HttpRunnable implements Runnable {

    private static final String LOG_TAG = HttpRunnable.class.getSimpleName();


    private final Request mRequest;
    private final OnResponseCallback mCallback;
    private volatile Call mCall;
    private volatile boolean canceled;

    public HttpRunnable(Request request, OnResponseCallback callback) {
        mRequest = request;
        mCallback = callback;
    }

    @Override
    public void run() {
        if (canceled) return;

        mCall = HttpClient.getInstance().newCall(mRequest);

        try {
            Response response = mCall.execute();
            if (canceled) return;

            if (response.isSuccessful()) {
                mCallback.onResponseSuccess(response);
            } else {
                mCallback.onResponseFail(response);
            }
        } catch (IOException e) {
            Logger.d(LOG_TAG, e);
            // won't to call it back if it's canceled
            if (!mCall.isCanceled())
                mCallback.onNetworkFail();
        }

    }

    /**
     * Cancel the runnable.
     *
     * Once it is being canceled, the callback will never be called and if it is canceled before
     * {@link #run()} being executed, {@link #run()} will immediately return.
     */
    public void cancel() {
        if (mCall != null && !mCall.isCanceled())
            mCall.cancel();
        canceled = true;
    }
}
