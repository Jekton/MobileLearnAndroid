package com.jekton.mobilelearn.activity.login;

import android.support.annotation.NonNull;

import com.jekton.mobilelearn.common.dv.network.SimpleDocument;
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
class LoginDocument extends SimpleDocument<LoginViewOps>
        implements LoginDocumentOps {

    private String mEmail;
    private String mPassword;

    @Override
    public void onLogin(String email, String password) {
        mEmail = email;
        mPassword = password;

        doPost(new LoginPostRunnable(email, password, this));
    }

    @Override
    public void onResponseSuccess(Response response) {
        LoginViewOps view = getView();
        if (view != null) {
            view.onLoginSuccess(mEmail, mPassword);
        }
    }



    private static class LoginPostRunnable extends AbstractHttpRunnable {

        private final Request mRequest;

        public LoginPostRunnable(String email,
                                 String password,
                                 OnResponseCallback callback) {
            super(callback);

            // This is a "post" Runnable, and the Request is always expected to be created
            RequestBody formBody = new FormBody.Builder()
                    .add("email", email)
                    .add("password", password)
                    .build();
            mRequest = new Request.Builder()
                    .url(UrlConstants.LOGIN)
                    .post(formBody)
                    .build();
        }

        @Override
        protected @NonNull
        Request makeRequest() {
            return mRequest;
        }
    }
}
