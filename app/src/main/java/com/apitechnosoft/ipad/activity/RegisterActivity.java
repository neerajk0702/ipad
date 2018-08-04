package com.apitechnosoft.ipad.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.utils.FontManager;

public class RegisterActivity extends AppCompatActivity {
    Button btnLogIn;
    Typeface materialdesignicons_font;
    EditText edt_phone, edt_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadView();
        datatoView();
    }
    //get view ids
    public void loadView() {
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        //edt_password = (EditText) findViewById(R.id.edt_password);
       // edt_phone = (EditText) findViewById(R.id.edt_phone);
    }

    //get data from UI
    public void datatoView() {
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  String password = edt_password.getText().toString();
               // String email = edt_phone.getText().toString();
                Intent intentLoggedIn = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intentLoggedIn);
            }
        });

    }
}
