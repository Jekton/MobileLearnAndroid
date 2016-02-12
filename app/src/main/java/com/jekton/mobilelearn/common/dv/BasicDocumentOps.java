package com.jekton.mobilelearn.common.dv;

/**
 * @author Jekton
 *
 * DocumentOps of the Document-View framework must extend this interface
 */
public interface BasicDocumentOps<ViewOps> {

    /**
     * called when the "view" is going to die
     */
    void onDestroy();

    void setView(ViewOps view);
}
