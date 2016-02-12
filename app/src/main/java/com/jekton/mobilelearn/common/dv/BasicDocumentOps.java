package com.jekton.mobilelearn.common.dv;

/**
 * @author Jekton
 */
public interface BasicDocumentOps<ViewOps> {

    /**
     * called when the "view" is going to die
     */
    void onDestroy();

    void setView(ViewOps view);
}
