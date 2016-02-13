package com.jekton.mobilelearn.activity.register;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.jekton.mobilelearn.common.dv.AbstractDocument;
import com.jekton.mobilelearn.network.AbstractHttpRunnable;
import com.jekton.mobilelearn.network.OnResponseCallback;
import com.jekton.mobilelearn.network.UrlConstants;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Jekton
 */
public class RegisterOperator extends AbstractDocument<RegisterViewOps>
        implements RegisterDocumentOps, OnResponseCallback {

    private static final String LOG_TAG = RegisterOperator.class.getSimpleName();

    private String mEmail;
    private String mPassword;

    private RegisterPostRunnable mPostRunnable;

    @Override
    public void onRegister(String name, String email, String password) {
        mEmail = email;
        mPassword = password;

        mPostRunnable = new RegisterPostRunnable(name, email, password, this);
        AsyncTask.THREAD_POOL_EXECUTOR.execute(mPostRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPostRunnable.cancel();
    }


    @Override
    public void onResponseSuccess(Response response) {
        RegisterViewOps view = getView();
        if (view != null) {
            // we don't retrieve them in the corresponding EditText to prevent modifying while
            // the registration
            view.onRegisterSuccess(mEmail,mPassword);
        }
    }


    @Override
    public void onNetworkFail() {
        RegisterViewOps view = getView();
        if (view != null) {
            view.onNetworkFail();
        }
    }

    @Override
    public void onResponseFail(Response response) {
        RegisterViewOps view = getView();
        if (view != null) {
            view.onRegisterFail();
        }
    }



    private static class RegisterPostRunnable extends AbstractHttpRunnable {

        private Request mRequest;

        public RegisterPostRunnable(String name,
                                    String email,
                                    String password,
                                    OnResponseCallback callback) {
            super(callback);

            // This is a "post" Runnable, and the Request is always expected to be created
            RequestBody formBody = new FormBody.Builder()
                    .add("name", name)
                    .add("email", email)
                    .add("password", password)
                    .build();
            mRequest = new Request.Builder()
                    .url(UrlConstants.REGISTER)
                    .post(formBody)
                    .build();
        }

        @Override
        protected @NonNull Request makeRequest() {
            return mRequest;
        }
    }
}
