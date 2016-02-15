package com.jekton.mobilelearn.register;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.dv.network.SimpleHttpActivity;
import com.jekton.mobilelearn.common.network.CredentialStorage;
import com.jekton.mobilelearn.common.util.Toaster;

/**
 * @author Jekton
 */
public class RegisterActivity extends SimpleHttpActivity<RegisterViewOps, RegisterDocument>
        implements View.OnClickListener, RegisterViewOps {

    private static final String LOG_TAG = RegisterActivity.class.getSimpleName();

    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreateDocument(this, RegisterDocument.class);
        setContentView(R.layout.activity_register);

        mEditTextName = (EditText) findViewById(R.id.name);
        mEditTextEmail = (EditText) findViewById(R.id.email);
        mEditTextPassword = (EditText) findViewById(R.id.password);

        findViewById(R.id.register).setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        showDialog();
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


    @Override
    public void onPostActionFail() {
        showToastAndDismissDialog(R.string.msg_register_fail);
    }

    @Override
    public void onNetworkFail() {
        showToastAndDismissDialog(R.string.err_network_error);
    }
}
