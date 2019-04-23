package com.example.maxim.myproject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maxim.myproject.utils.MySharedPrefs;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText pass, login;
    // Attempt to invoke virtual method 'android.content.SharedPreferences android.content.Context.getSharedPreference
    // выше не полная информация об ошибки, нужно весь текст:
    // Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'android.content.SharedPreferences android.content.Context.getSharedPreferences(java.lang.String, int)' on a null object
    // Что примерно: ошибка нулового указателя: пытаешься вызвать метод у объекта, который null.
    // Причина: объект context еще не готов к использованию. Надо перенести этот код в место вызыва или в onCreate
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // вот так
        sharedPreferences = MySharedPrefs.getAppPreferences(this);

        View.OnClickListener oclBtnReg = new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View v) {
                pass = findViewById(R.id.password);
                login = findViewById(R.id.email);
                String passT = pass.getText().toString();
                String loginT = login.getText().toString();
                // или так
                // SharedPreferences sharedPreferences = MySharedPrefs.getAppPreferences(this);
                String savedLogin = sharedPreferences.getString(ActivityReg.SAVED_LOGIN, "");
                String savedPassword = sharedPreferences.getString(ActivityReg.SAVED_PASSWORD, "");
                //pass.setText(savedLogin);
                //login.setText(savedPassword);
                if (loginT.equals(savedLogin) && passT.equals(savedPassword)) {
                    Toast toast2 = Toast.makeText(getApplicationContext(),
                            "Авторизация успешно пройдена!", Toast.LENGTH_LONG);
                    toast2.show();
                } else {
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


