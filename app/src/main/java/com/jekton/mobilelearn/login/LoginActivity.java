package com.jekton.mobilelearn.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.dv.network.SimpleHttpActivity;
import com.jekton.mobilelearn.common.network.CredentialStorage;
import com.jekton.mobilelearn.common.util.NavigationUtil;
import com.jekton.mobilelearn.common.util.Toaster;
import com.jekton.mobilelearn.register.RegisterActivity;

/**
 * @author Jekton
 */
public class LoginActivity extends SimpleHttpActivity<LoginViewOps, LoginDocument>
        implements LoginViewOps, View.OnClickListener {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        super.onCreateDocument(this, LoginDocument.class);

        initView();
    }

    private void initView() {
        mEmailEditText = (EditText) findViewById(R.id.email);
        mPasswordEditText = (EditText) findViewById(R.id.password);

        String[] credential = CredentialStorage.getCredential();
        mEmailEditText.setText(credential[0]);
        mPasswordEditText.setText(credential[1]);

        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);
    }

    @Override
    public void onLoginSuccess(final String email, final String password) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toaster.showShort(LoginActivity.this, R.string.msg_success_login);
                CredentialStorage.storeCredential(email, password);
                NavigationUtil.gotoMainActivity(LoginActivity.this);
            }
        });
    }



    @Override
    public void onPostActionFail() {
        showToastAndDismissDialog(R.string.msg_fail_login);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            showDialog();
            getDocument().onLogin(mEmailEditText.getText().toString(),
                                  mPasswordEditText.getText().toString());
        } else {
            getDocument().onDestroy();
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }
}
