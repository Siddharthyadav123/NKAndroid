package com.netkoin.app.screens.signinoption;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.netkoin.app.R;
import com.netkoin.app.controller.ActivityController;

import org.w3c.dom.Text;

public class SignInOptionActivity extends Activity {
    private TextView loginTextView;
    private TextView signUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_login_options_layout);
        initViews();
        registerEvents();
    }


    private void initViews() {
        loginTextView = (TextView) findViewById(R.id.loginTextView);
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
    }

    private void registerEvents() {
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController.getInstance().handleEvent(SignInOptionActivity.this, ActivityController.ACTIVITY_SIGN_IN);
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController.getInstance().handleEvent(SignInOptionActivity.this, ActivityController.ACTIVITY_SIGN_UP);

            }
        });
    }

}
