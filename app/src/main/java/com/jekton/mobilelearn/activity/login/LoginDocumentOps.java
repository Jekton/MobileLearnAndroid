package com.jekton.mobilelearn.activity.login;

import com.jekton.mobilelearn.common.dv.BasicDocumentOps;

/**
 * @author Jekton
 */
interface LoginDocumentOps extends BasicDocumentOps<LoginViewOps> {

    void onLogin(String email, String password);
}
