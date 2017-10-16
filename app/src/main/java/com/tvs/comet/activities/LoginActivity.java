package com.tvs.comet.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tvs.comet.R;


public class LoginActivity extends AppCompatActivity {

    TextInputLayout til_username, til_password;
    EditText et_username, et_password;
    TextView tv_forgot_password;
    Button btn_sign_in;

    Typeface font_RalewayBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
            }
        });


    }

    private void init() {
        font_RalewayBold = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");

        btn_sign_in = (Button) findViewById(R.id.btn_sign_in);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        til_username = (TextInputLayout) findViewById(R.id.til_username);
        til_password = (TextInputLayout) findViewById(R.id.til_password);
        til_username.setTypeface(font_RalewayBold);
        til_password.setTypeface(font_RalewayBold);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_enter, R.anim.push_right_exit);
    }
}
