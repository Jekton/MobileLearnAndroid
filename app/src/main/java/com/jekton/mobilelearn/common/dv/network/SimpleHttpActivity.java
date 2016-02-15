package com.jekton.mobilelearn.common.dv.network;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.activity.DialogEnabledActivity;

/**
 * @author Jekton
 *
 * This class is design to interact with {@link SimpleHttpDocument} and so it's expecting the
 * parameter type Document to extend {@link SimpleHttpDocument}
 */
public abstract class SimpleHttpActivity<ViewOps extends OnDocumentFail,
                                Document extends SimpleHttpDocument<ViewOps>>
        extends DialogEnabledActivity<ViewOps, Document>
        implements OnDocumentFail {


    @Override
    public void onBackPressed() {
        if (isDialogShowing()) {
            closeDialog();
            getDocument().cancelNetworkOp();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onNetworkFail() {
        showToastAndDismissDialog(R.string.err_network_error);
    }
}
