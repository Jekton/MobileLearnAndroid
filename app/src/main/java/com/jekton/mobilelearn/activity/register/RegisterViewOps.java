package com.jekton.mobilelearn.activity.register;

/**
 * @author Jekton
 */
public interface RegisterViewOps {

    void onRegisterSuccess(String email, String password);
    void onRegisterFail();
    void onNetworkFail();
}
