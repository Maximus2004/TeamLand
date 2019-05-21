package com.example.maxim.myproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityReg extends AppCompatActivity {
    static final String SAVED_LOGIN = "LOGIN";
    static final String SAVED_PASSWORD = "PASSWORD";
    boolean checkingSecondPasswordEdit = true;
    boolean checkingFirstPasswordEdit = true;
    boolean checkingNickPasswordEdit = true;
    boolean checkingDescribtionPasswordEdit = true;
    String firstPasswordEditString, describtionEditString, nickEditString, mainCountClientsString;
    Button registrationButton, authButton;
    EditText theFirstPassword, theSecondPassword, nickEditText, describtion;
    TextView firstPasswordText, secondPasswordText, nickText, describtionText;
    int mainCountClientsInt = 0;
    boolean isLoginAlreadyInUse = true;
    String dataSnapshot2 = "0";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        View.OnClickListener oclBtnReg = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSecondPassword();
                checkNicks();
                checkDescription();

                if (!checkingSecondPasswordEdit || !checkingFirstPasswordEdit || !checkingNickPasswordEdit || !checkingDescribtionPasswordEdit) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Форма заполенна некорректно", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast2 = Toast.makeText(getApplicationContext(),
                            "Регистрация успешно пройдена!", Toast.LENGTH_LONG);
                    toast2.show();
                    saveData();
                    mainCountClientsInt++;
                    mainCountClientsString = String.valueOf(mainCountClientsInt);
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    ValueEventListener listenerAtOnce = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Toast.makeText(getApplicationContext(), "onDataChange() в listenerAtOnce", Toast.LENGTH_SHORT).show();
                            // сбрасываем флаг
                            isLoginAlreadyInUse = false;
                            int maxId = Integer.parseInt(dataSnapshot.child("maxId").getValue().toString());
                            for (int i = 0; i < maxId; i++) {    //i < id
                                Object entity = dataSnapshot.child("client" + i).child("login").getValue();
                                if (entity != null && entity.toString().equals(nickEditString)) {
                                    // нашли похожий, останавливаем цикл
                                    isLoginAlreadyInUse = true;
                                    break;
                                }
                            }

                            if (isLoginAlreadyInUse) {
                                Toast.makeText(getApplicationContext(), "Поменяйте имя пользователя", Toast.LENGTH_LONG).show();
                                nickText.setTextColor(Color.RED);
                            } else {
                                Toast.makeText(getApplicationContext(), "Ники не совпали и всё нормусь", Toast.LENGTH_SHORT).show();
                                nickText.setTextColor(Color.BLACK);
                                mainCountClientsInt++;
                                mainCountClientsString = String.valueOf(mainCountClientsInt);
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                // очень странная логика с dataSnapshot2
                                dataSnapshot2 = dataSnapshot.child("maxId").getValue().toString();
                                writeNewUser(dataSnapshot.child("maxId").getValue().toString(), nickEditString, firstPasswordEditString, describtionEditString);
                                mDatabase.child("maxId").setValue(Integer.parseInt(dataSnapshot.child("maxId").getValue().toString()) + 1);
                                Intent intent2 = new Intent(ActivityReg.this, MostMainActivity.class);
                                intent2.putExtra(CreateApplication.PARAM_USER_NAME, nickEditString);
                                startActivity(intent2);
                                Intent intent = new Intent(ActivityReg.this, MostMainActivity.class);
                                startActivity(intent);
                                // финишируем активити при успешной регистрации
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Возникла ошибка", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    };
                    mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
                }
            }
        };

        registrationButton = findViewById(R.id.button4);
        registrationButton.setOnClickListener(oclBtnReg);

        View.OnClickListener oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityReg.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        authButton = findViewById(R.id.button5);
        authButton.setOnClickListener(oclBtn);

    }

    void saveData() {
        // преференсы можно сделать локальные
        // текущий вызов достает преференсы ТОЛЬКО для активити
        // чтобы они были для всего приложения нужно вызывать getPreferences("текст1", MODE_PRIVATE)
        // и в другом активити вызывать также getPreferences("текст1", MODE_PRIVATE)
        // вместо "текст1" может быть любой ключ, но одинаковый в обоих вызовах
        // SAVED_TEXT и SAVED_NUM должны быть static и их можно будет вызывать так: ActivityReg.SAVED_TEXT
        SharedPreferences sharedPreferences = getSharedPreferences("ALL_APP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SAVED_LOGIN, nickEditString);
        editor.putString(SAVED_PASSWORD, firstPasswordEditString);
        editor.commit();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    private void writeNewUser(String userId, String login, String password, String description) {
        User user = new User(userId, login, password, description);
        mDatabase.child("client" + dataSnapshot2).setValue(user);
        /*Toast toast = Toast.makeText(getApplicationContext(),
                "Зашёл в writeNewUser", Toast.LENGTH_SHORT);
        toast.show();*/
    }

    void checkSecondPassword() {
        firstPasswordText = findViewById(R.id.textView14);
        secondPasswordText = findViewById(R.id.textView15);
        theFirstPassword = findViewById(R.id.editText4);
        theSecondPassword = findViewById(R.id.editText5);
        firstPasswordEditString = theFirstPassword.getText().toString();
        String secondPasswordEditString = theSecondPassword.getText().toString();
        if (secondPasswordEditString.equals("") || !secondPasswordEditString.equals(firstPasswordEditString)) {
            secondPasswordText.setTextColor(Color.RED);
            checkingSecondPasswordEdit = false;
        }
        if (secondPasswordEditString.equals(firstPasswordEditString)) {
            secondPasswordText.setTextColor(Color.BLACK);
            checkingSecondPasswordEdit = true;
        }

        if (firstPasswordEditString.equals("")) {
            firstPasswordText.setTextColor(Color.RED);
            checkingFirstPasswordEdit = false;
        } else {
            firstPasswordText.setTextColor(Color.BLACK);
            checkingFirstPasswordEdit = true;
        }
    }

    void checkNicks() {
        nickText = findViewById(R.id.textView13);
        nickEditText = findViewById(R.id.editText3);
        nickEditString = nickEditText.getText().toString();
        if (nickEditString.equals("")) {
            nickText.setTextColor(Color.RED);
            checkingNickPasswordEdit = false;
        } else {
            nickText.setTextColor(Color.BLACK);
            checkingNickPasswordEdit = true;
        }
    }

    void checkDescription() {
        describtionText = findViewById(R.id.textView16);
        describtion = findViewById(R.id.editText7);
        describtionEditString = describtion.getText().toString();
        if (describtionEditString.equals("")) {
            describtionText.setTextColor(Color.RED);
            checkingDescribtionPasswordEdit = false;
        } else {
            describtionText.setTextColor(Color.BLACK);
            checkingDescribtionPasswordEdit = true;
        }
    }
}