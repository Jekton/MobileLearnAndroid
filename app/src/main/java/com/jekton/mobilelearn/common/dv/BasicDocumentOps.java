package com.jekton.mobilelearn.common.dv;

/**
 * DocumentOps of the Document-View framework must extend this interface
 *
 * @author Jekton
 */
public interface BasicDocumentOps<ViewOps> {

    void onCreate();

    /**
     * called when the "view" is going to die
     */
    void onDestroy();

    void setView(ViewOps view);
}
