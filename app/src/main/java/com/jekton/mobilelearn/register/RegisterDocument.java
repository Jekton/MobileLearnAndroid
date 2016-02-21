package com.jekton.mobilelearn.register;

import android.support.annotation.NonNull;

import com.jekton.mobilelearn.common.dv.network.SimpleHttpDocument;
import com.jekton.mobilelearn.common.network.AbstractHttpRunnable;
import com.jekton.mobilelearn.common.network.operator.OnResponseCallback;
import com.jekton.mobilelearn.network.UrlConstants;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Jekton
 */
class RegisterDocument extends SimpleHttpDocument<RegisterViewOps>
        implements RegisterDocumentOps {

    private static final String LOG_TAG = RegisterDocument.class.getSimpleName();

    private String mEmail;
    private String mPassword;


    @Override
    public void onRegister(String name, String email, String password) {
        mEmail = email;
        mPassword = password;

        doHttpRequest(new RegisterPostRunnable(name, email, password, this));
    }


    @Override
    public void onResponseSuccess(Response response) {
        RegisterViewOps view = getView();
        if (view != null) {
            // we don't retrieve them in the corresponding EditText to prevent modifying while
            // the registration
            view.onRegisterSuccess(mEmail, mPassword);
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
