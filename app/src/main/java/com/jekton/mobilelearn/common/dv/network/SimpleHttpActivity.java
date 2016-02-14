package com.jekton.mobilelearn.common.dv.network;

import com.jekton.mobilelearn.common.activity.DialogEnabledActivity;

/**
 * @author Jekton
 */
public class SimpleHttpActivity<ViewOps extends OnDocumentFail,
                                DocumentOps extends SimpleHttpDocument<ViewOps>>
        extends DialogEnabledActivity<ViewOps, DocumentOps> {


    @Override
    public void onBackPressed() {
        if (isDialogShowing()) {
            closeDialog();
            getDocument().cancelNetworkOp();
        } else {
            super.onBackPressed();
        }
    }
}
