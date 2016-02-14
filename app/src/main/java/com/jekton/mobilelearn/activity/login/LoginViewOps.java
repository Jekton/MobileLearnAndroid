package com.jekton.mobilelearn.activity.login;

import com.jekton.mobilelearn.common.dv.network.OnDocumentFail;

/**
 * @author Jekton
 */
interface LoginViewOps extends OnDocumentFail {

    void onLoginSuccess(String email, String password);

}
