package com.jekton.mobilelearn.network;

import okhttp3.Response;

/**
 * @author Jekton
 */
public interface OnResponseCallback {

    void onNetworkFail();

    void onResponseSuccess(Response response);

    void onResponseFail(Response response);
}
