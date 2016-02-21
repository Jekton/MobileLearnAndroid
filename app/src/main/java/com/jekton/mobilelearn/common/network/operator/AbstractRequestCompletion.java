package com.jekton.mobilelearn.common.network.operator;

import okhttp3.Response;

/**
 * @author Jekton
 */
abstract class AbstractRequestCompletion implements OnResponseCallback {

    private final OnResponseCallback mOriginCallback;

    public AbstractRequestCompletion(OnResponseCallback callback) {
        mOriginCallback = callback;
    }

    /**
     * subclass need to override this method to remove the the request from
     * the {@link NetworkOperator}.
     *
     * @return true if need to forward to the origin callback
     */
    protected abstract boolean cleanAndCheck();

    @Override
    public void onResponseSuccess(Response response) {
        if (cleanAndCheck()) {
            mOriginCallback.onResponseSuccess(response);
        }
    }

    @Override
    public void onNetworkFail() {
        if (cleanAndCheck()) {
            mOriginCallback.onNetworkFail();
        }
    }

    @Override
    public void onResponseFail(Response response) {
        if (cleanAndCheck()) {
            mOriginCallback.onResponseFail(response);
        }
    }

}
