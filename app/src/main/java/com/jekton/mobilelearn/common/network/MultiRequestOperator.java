package com.jekton.mobilelearn.common.network;

import android.support.annotation.NonNull;

import okhttp3.Request;

/**
 * @author Jekton
 */
class MultiRequestOperator implements NetworkOperator {

    @Override
    public void executeRequest(@NonNull Request request,
                               @NonNull OnResponseCallback callback) {

    }

    @Override
    public void executeRequest(@NonNull Object key,
                               @NonNull Request request,
                               @NonNull OnResponseCallback callback) {

    }

    @Override
    public void cancelRequest(@NonNull Object key) {

    }

    @Override
    public void cancelRequests() {

    }
}
