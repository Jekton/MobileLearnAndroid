package com.jekton.mobilelearn.activity.register;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.EditText;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.dv.GenericActivity;
import com.jekton.mobilelearn.common.util.Toaster;
import com.jekton.mobilelearn.network.CredentialStorage;

/**
 * @author Jekton
 */
public class RegisterActivity extends GenericActivity<RegisterViewOps, RegisterDocumentOps>
        implements View.OnClickListener, RegisterViewOps {

    private static final String LOG_TAG = RegisterActivity.class.getSimpleName();

    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;

    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDocument(this, RegisterOperator.class);
        setContentView(R.layout.activity_register);

        mEditTextName = (EditText) findViewById(R.id.name);
        mEditTextEmail = (EditText) findViewById(R.id.email);
        mEditTextPassword = (EditText) findViewById(R.id.password);

        findViewById(R.id.register).setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.show();
        getDocument().onRegister(mEditTextName.getText().toString(),
                                 mEditTextEmail.getText().toString(),
                                 mEditTextPassword.getText().toString());
    }

    @Override
    public void onRegisterSuccess(final String email, final String password) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toaster.showShort(RegisterActivity.this, R.string.msg_register_success);
                CredentialStorage.storeCredential(email, password);
                // TODO: 2/13/2016  go to add course page
            }
        });
    }

    private void showToastAndDismissDialog(@StringRes final int msgId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toaster.showShort(RegisterActivity.this, msgId);
                mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onRegisterFail() {
        showToastAndDismissDialog(R.string.msg_register_fail);
    }

    @Override
    public void onNetworkFail() {
        showToastAndDismissDialog(R.string.err_network_error);
    }
}
