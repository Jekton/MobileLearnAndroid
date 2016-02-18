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

    public HttpRunnable(Request request, OnResponseCallback callback) {
        mRequest = request;
        mCallback = callback;
    }

    @Override
    public void run() {
        mCall = HttpClient.getInstance().newCall(mRequest);

        try {
            Response response = mCall.execute();
            if (response.isSuccessful()) {
                mCallback.onResponseSuccess(response);
            } else {
                mCallback.onResponseFail(response);
            }
        } catch (IOException e) {
            Logger.d(LOG_TAG, e);
            // won't to call it back if it's canceled
            if (mCall.isCanceled())
                mCallback.onNetworkFail();
        }

    }

    public void cancel() {
        if (!mCall.isCanceled())
            mCall.cancel();
    }
}
