package com.jekton.mobilelearn.activity.register;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jekton.mobilelearn.R;
import com.jekton.mobilelearn.common.dv.GenericActivity;
import com.jekton.mobilelearn.network.HttpClient;
import com.jekton.mobilelearn.network.UrlConstants;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Jekton
 */
public class RegisterActivity extends GenericActivity<RegisterViewOps, RegisterDocumentOps>
        implements View.OnClickListener, RegisterViewOps {

    private static final String LOG_TAG = RegisterActivity.class.getSimpleName();

    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;

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

    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFail(String msg) {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    private void test() {
        try {
            Request request = new Request.Builder()
                    .url(UrlConstants.GET_TAKEN_COURSES)
                    .build();

            Response response = HttpClient.getInstance().newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e(LOG_TAG, response.body().string());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "", e);
        }
    }
}
