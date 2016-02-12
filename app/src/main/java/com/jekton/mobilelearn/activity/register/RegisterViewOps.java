package com.jekton.mobilelearn.activity.register;

import com.jekton.mobilelearn.common.dv.ContextView;

/**
 * @author Jekton
 */
public interface RegisterViewOps extends ContextView {

    void onRegisterSuccess();
    void onRegisterFail(String msg);
}
