package com.example.maxim.myproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Здесь ты в асинхронном потоке делаешь какую-то логику. Почитай про asyncTask

    }
}
