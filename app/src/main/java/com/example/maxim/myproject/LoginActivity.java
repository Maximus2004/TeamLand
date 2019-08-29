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

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    private DatabaseReference mDatabase;
    boolean isUserRegistrated = true;
    EditText pass, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button buttonRegistration = findViewById(R.id.buttonRegistration);
        View.OnClickListener oclBtnRegistr = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ActivityReg.class);
                startActivity(intent);
            }
        };
        buttonRegistration.setOnClickListener(oclBtnRegistr);
        View.OnClickListener oclBtnReg = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = findViewById(R.id.password);
                login = findViewById(R.id.email);
                final String passT = pass.getText().toString();
                final String loginT = login.getText().toString();
                ValueEventListener listenerAtOnce = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Toast.makeText(getApplicationContext(), "onDataChange() в listenerAtOnce", Toast.LENGTH_SHORT).show();
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
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Вход успешно выполнен", Toast.LENGTH_SHORT);
                                toast.show();
                                //finish();
                                break;
                            }
                        }
                        if (isUserRegistrated){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Вы не подключены к сети или такого пользователя не существует", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Ошибка!" + databaseError.toString() + "", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                };
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
                if (!isUserRegistrated){
                    Intent intent = new Intent(LoginActivity.this, MostMainActivity.class);
                    startActivity(intent);
                    // передаем логин пользователя в главное активити
                    intent.putExtra(MostMainActivity.PARAM_USER_NAME, login.toString());
                    // финишируем активити при успешной авторизации
                    //finish();
                }
            }
        };
        btnLogin = findViewById(R.id.email_sign_in_button);
        btnLogin.setOnClickListener(oclBtnReg);
    }
}