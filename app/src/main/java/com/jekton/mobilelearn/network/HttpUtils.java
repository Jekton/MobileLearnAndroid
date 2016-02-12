package com.jekton.mobilelearn.network;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author Jekton
 */
public class HttpUtils {

    static Request makeLoginRequest(String email, String password) {
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        return new Request.Builder()
                .url(UrlConstants.LOGIN)
                .post(formBody)
                .build();
    }

    static Request makeLoginRequest() {
        String[] credential = CredentialStorage.getCredential();

        return makeLoginRequest(credential[0], credential[1]);
    }

    private HttpUtils() {
        throw new AssertionError("Cannot instantiate this class!");
    }
}
