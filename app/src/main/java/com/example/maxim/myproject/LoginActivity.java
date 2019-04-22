package com.example.maxim.myproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity{
    Button btnLogin;
    EditText pass, login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View.OnClickListener oclBtnReg = new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View v) {
                pass = findViewById(R.id.password);
                login = findViewById(R.id.email);
                String passT = pass.getText().toString();
                String loginT = login.getText().toString();

                // в ActivityReg указал, как вызывать preference
                ActivityReg.sharedPreferences = getPreferences(MODE_PRIVATE);
                String savedLogin = ActivityReg.sharedPreferences.getString(ActivityReg.SAVED_LOGIN, "");
                String savedPassword = ActivityReg.sharedPreferences.getString(ActivityReg.SAVED_PASSWORD, "");
                //pass.setText(savedLogin);
                //login.setText(savedPassword);
                if (loginT.equals(savedLogin) && passT.equals(savedPassword)){
                    Toast toast2 = Toast.makeText(getApplicationContext(),
                            "Авторизация успешно пройдена!", Toast.LENGTH_LONG);
                    toast2.show();
                }
                else {
                    Toast toast3 = Toast.makeText(getApplicationContext(),
                            "Такого пароля или имени пользователя не существует в системе", Toast.LENGTH_LONG);
                    toast3.show();
                }
            }
        };
        btnLogin = findViewById(R.id.email_sign_in_button);
        btnLogin.setOnClickListener(oclBtnReg);
    }
}


