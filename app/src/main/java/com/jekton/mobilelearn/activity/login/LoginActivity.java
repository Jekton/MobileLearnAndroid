package com.jekton.mobilelearn.activity.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.activity.DialogEnabledActivity;
import com.jekton.mobilelearn.common.util.Toaster;
import com.jekton.mobilelearn.network.CredentialStorage;

/**
 * @author Jekton
 */
public class LoginActivity extends DialogEnabledActivity<LoginViewOps, LoginDocumentOps>
        implements LoginViewOps, View.OnClickListener {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = (EditText) findViewById(R.id.email);
        mPasswordEditText = (EditText) findViewById(R.id.password);

        super.onCreateDocument(this, LoginDocument.class);
        findViewById(R.id.register).setOnClickListener(this);
    }

    @Override
    public void onLoginSuccess(final String email, final String password) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toaster.showShort(LoginActivity.this, R.string.msg_login_success);
                CredentialStorage.storeCredential(email, password);
                // TODO: 2/13/2016  go to user taken course page
            }
        });
    }

    @Override
    public void onPostActionFail() {
        showToastAndDismissDialog(R.string.msg_login_fail);
    }

    @Override
    public void onNetworkFail() {
        showToastAndDismissDialog(R.string.err_network_error);
    }

    @Override
    public void onClick(View v) {
        showDialog();
        getDocument().onLogin(mEmailEditText.getText().toString(),
                              mPasswordEditText.getText().toString());
    }
}
