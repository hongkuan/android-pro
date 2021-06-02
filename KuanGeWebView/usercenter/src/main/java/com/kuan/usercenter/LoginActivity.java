package com.kuan.usercenter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.kuan.common.eventbus.LoginEvent;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin, btnLinkToRegister, btnForgotPass;
    private TextInputLayout inputEmail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.lTextEmail);
        inputPassword = findViewById(R.id.lTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLinkToRegister = findViewById(R.id.btnLinkToRegisterScreen);
        btnForgotPass = findViewById(R.id.btnForgotPassword);

        initListener();
    }

    private void initListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Login button Click Event
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        EventBus.getDefault().post(new LoginEvent(inputEmail.getEditText().getText().toString()));
                        finish();
                    }
                });
            }
        });
    }
}