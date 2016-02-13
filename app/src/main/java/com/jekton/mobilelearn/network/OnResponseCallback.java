package com.jekton.mobilelearn.network;

import okhttp3.Response;

/**
 * @author Jekton
 */
public interface OnResponseCallback {

    void onResponseSuccess(Response response);

    void onNetworkFail();

    void onResponseFail(Response response);
}
