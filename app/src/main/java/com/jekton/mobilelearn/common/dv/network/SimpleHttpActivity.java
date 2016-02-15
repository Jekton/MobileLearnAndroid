package com.jekton.mobilelearn.common.dv.network;

import com.jekton.mobilelearn.common.activity.DialogEnabledActivity;

/**
 * @author Jekton
 *
 * This class is design to interact with {@link SimpleHttpDocument} and so it's expecting the
 * parameter type Document to extend {@link SimpleHttpDocument}
 */
public class SimpleHttpActivity<ViewOps extends OnDocumentFail,
                                Document extends SimpleHttpDocument<ViewOps>>
        extends DialogEnabledActivity<ViewOps, Document> {


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
