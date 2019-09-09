package com.example.maxim.myproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    private DatabaseReference mDatabase;
    boolean isUserRegistrated = true;
    EditText pass, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupButtonReg();
        setupButtonLogin();

    }
    private void setupButtonReg() {
        Button buttonRegistration = findViewById(R.id.buttonRegistration);
        View.OnClickListener oclBtnRegistr = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ActivityReg.class);
                startActivity(intent);
            }
        };
        buttonRegistration.setOnClickListener(oclBtnRegistr);
    }

    private void setupButtonLogin() {
        View.OnClickListener oclBtnReg = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueEventListener listenerAtOnce = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        checkingUser(dataSnapshot);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        showToast("Ошибка!", databaseError.toString());
                    }
                };
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
                userRegistration();
            }
        };
        btnLogin = findViewById(R.id.email_sign_in_button);
        btnLogin.setOnClickListener(oclBtnReg);
    }

    private void userRegistration() {
        if (!isUserRegistrated){
            Intent intent = new Intent(LoginActivity.this, MostMainActivity.class);
            startActivity(intent);
            // передаем логин пользователя в главное активити
            intent.putExtra(MostMainActivity.PARAM_USER_NAME, login.toString());
            // финишируем активити при успешной авторизации
            finish();
        }
    }

    private void checkingUser(DataSnapshot dataSnapshot) {
        pass = findViewById(R.id.password);
        login = findViewById(R.id.email);
        final String passT = pass.getText().toString();
        final String loginT = login.getText().toString();

        int maxId = Integer.parseInt(dataSnapshot.child("maxId").getValue().toString());
        for (int i = 0; i < maxId; i++) {    //i < id
            Object login = dataSnapshot.child("client" + i).child("login").getValue();
            Object password = dataSnapshot.child("client" + i).child("password").getValue();
            if (login != null && password != null && login.equals(loginT) && password.toString().equals(passT)) {
                // нашли совпадение, останавливаем цикл
                isUserRegistrated = false;
                Intent intent = new Intent(LoginActivity.this, MostMainActivity.class);
                intent.putExtra(MostMainActivity.PARAM_USER_NAME, loginT);
                startActivity(intent);
                showToast("Вход успешно выполнен", "");
                break;
            }
        }
        if (isUserRegistrated){
            showToast("Вы не подключены к сети или такого пользователя не существует", "");
        }
    }

    private void showToast(String text, String databaseError) {
        Toast toast = Toast.makeText(getApplicationContext(),
                text + databaseError + "", Toast.LENGTH_SHORT);
        toast.show();
    }
}