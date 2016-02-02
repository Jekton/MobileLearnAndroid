package com.jekton.mobilelearn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * @author Jekton
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEditTextName = (EditText) findViewById(R.id.name);
        mEditTextEmail = (EditText) findViewById(R.id.email);
        mEditTextPassword = (EditText) findViewById(R.id.password);

        findViewById(R.id.register).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        //
    }
}
