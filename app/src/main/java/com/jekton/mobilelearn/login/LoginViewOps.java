package com.jekton.mobilelearn.login;

import com.jekton.mobilelearn.common.dv.network.OnDocumentFail;

/**
 * @author Jekton
 */
interface LoginViewOps extends OnDocumentFail {

    void onLoginSuccess(String email, String password);

}
