package com.jekton.mobilelearn.activity.register;

import com.jekton.mobilelearn.common.dv.BasicDocumentOps;

/**
 * @author Jekton
 */
public interface RegisterDocumentOps extends BasicDocumentOps<RegisterViewOps> {

    void onRegister(String name, String email, String password);

}
