package com.jekton.mobilelearn.activity.register;

import com.jekton.mobilelearn.common.dv.network.OnDocumentFail;

/**
 * @author Jekton
 */
interface RegisterViewOps extends OnDocumentFail {

    void onRegisterSuccess(String email, String password);

}
