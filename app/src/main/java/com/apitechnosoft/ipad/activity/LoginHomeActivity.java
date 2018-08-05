package com.apitechnosoft.ipad.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.apitechnosoft.ipad.R;

public class LoginHomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);
        loadView();
        datatoView();
    }

    //get view ids
    public void loadView() {
        Button login = (Button) findViewById(R.id.login);
        Button signup = (Button) findViewById(R.id.signup);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        LinearLayout joinLayout = findViewById(R.id.joinLayout);
        joinLayout.setOnClickListener(this);
    }

    //get data from UI
    public void datatoView() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                Intent i = new Intent(LoginHomeActivity.this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.signup:
                Intent intent = new Intent(LoginHomeActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.joinLayout:
                Intent intentjoin = new Intent(LoginHomeActivity.this, RegisterActivity.class);
                startActivity(intentjoin);
                break;
        }
    }
}
