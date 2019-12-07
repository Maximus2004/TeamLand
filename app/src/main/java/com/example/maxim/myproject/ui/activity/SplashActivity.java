package com.example.maxim.myproject.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.maxim.myproject.R;

import com.example.maxim.myproject.DBHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        DBHelper.fillDataAllOther();
        finish();
    }
}
