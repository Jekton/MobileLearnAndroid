package com.jekton.mobilelearn.common.activity;

import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;

import com.jekton.mobilelearn.common.dv.BasicDocumentOps;
import com.jekton.mobilelearn.common.dv.GenericActivity;
import com.jekton.mobilelearn.common.util.Toaster;

/**
 * @author Jekton
 */
public class DialogEnabledActivity<ViewOps, DocumentOps extends BasicDocumentOps<ViewOps>>
        extends GenericActivity<ViewOps, DocumentOps> {

    private ProgressDialog mProgressDialog;

    @UiThread
    protected void showDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.show();
    }

    @UiThread
    protected void closeDialog() {
        mProgressDialog.dismiss();
    }

    @UiThread
    protected void cancelDialog() {
        mProgressDialog.cancel();
    }


    protected void showToastAndDismissDialog(@StringRes final int msgId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toaster.showShort(DialogEnabledActivity.this, msgId);
                closeDialog();
            }
        });
    }
}
