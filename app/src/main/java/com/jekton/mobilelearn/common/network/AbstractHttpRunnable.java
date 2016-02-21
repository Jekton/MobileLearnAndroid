package com.jekton.mobilelearn.common.network;

import android.support.annotation.NonNull;

import com.jekton.mobilelearn.common.network.operator.OnResponseCallback;
import com.jekton.mobilelearn.common.util.Logger;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @deprecated Use the {@link com.jekton.mobilelearn.common.network.operator.NetworkOperator}
 * framework instead
 *
 * @author Jekton
 */
public abstract class AbstractHttpRunnable implements Runnable {

    private static final String LOG_TAG = AbstractHttpRunnable.class.getSimpleName();

    private volatile Call mCall;
    private final OnResponseCallback mCallback;

    public AbstractHttpRunnable(OnResponseCallback callback) {
        mCallback = callback;
    }

    @Override
    public void run() {
        Request request = makeRequest();
        mCall = HttpClient.getInstance().newCall(request);

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
            if (!mCall.isCanceled())
                mCallback.onNetworkFail();
        }

    }

    public void cancel() {
        if (!mCall.isCanceled())
            mCall.cancel();
    }

    protected abstract @NonNull Request makeRequest();


}
