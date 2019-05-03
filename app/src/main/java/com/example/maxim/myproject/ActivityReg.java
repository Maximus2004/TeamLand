package com.example.maxim.myproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

public class ActivityReg extends AppCompatActivity {
    // не получается сделать локальными, потому что нельзя, чтобы были final
    boolean checkingSecondPasswordEdit = true;
    boolean checkingFirstPasswordEdit = true;
    boolean checkingNickPasswordEdit = true;
    boolean checkingDescribtionPasswordEdit = true;
    // должны быть static
    static final String SAVED_LOGIN = "LOGIN";
    static final String SAVED_PASSWORD = "PASSWORD";
    String nickEditString;
    String firstPasswordEditString;
    String describtionEditString;
    Button registrationButton, authButton;
    EditText theFirstPassword, theSecondPassword, nickEditText, describtion;
    TextView firstPasswordText, secondPasswordText, nickText, describtionText;
    private DatabaseReference mDatabase;
    String mainCountClientsString;
    int mainCountClientsInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // нечитабельно, разбить метод на несколько мелких
        //не забудь про проверку на совпадение ников
        View.OnClickListener oclBtnReg = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //исправить названия элементов на макете пока не могу, т. к. ещё не починили R.
                theFirstPassword = findViewById(R.id.editText4);
                theSecondPassword = findViewById(R.id.editText5);
                firstPasswordText = findViewById(R.id.textView14);
                secondPasswordText = findViewById(R.id.textView15);
                nickText = findViewById(R.id.textView13);
                nickEditText = findViewById(R.id.editText3);
                describtionText = findViewById(R.id.textView16);
                describtion = findViewById(R.id.editText7);
                nickEditString = nickEditText.getText().toString();
                describtionEditString = describtion.getText().toString();
                firstPasswordEditString = theFirstPassword.getText().toString();
                String secondPasswordEditString = theSecondPassword.getText().toString();
                // можно сделать один if
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
                if (nickEditString.equals("")) {
                    nickText.setTextColor(Color.RED);
                    checkingNickPasswordEdit = false;
                } else {
                    nickText.setTextColor(Color.BLACK);
                    checkingNickPasswordEdit = true;
                }
                if (describtionEditString.equals("")) {
                    describtionText.setTextColor(Color.RED);
                    checkingDescribtionPasswordEdit = false;
                } else {
                    describtionText.setTextColor(Color.BLACK);
                    checkingDescribtionPasswordEdit = true;
                }
                // сделать одним if
                if (!checkingSecondPasswordEdit || !checkingFirstPasswordEdit || !checkingNickPasswordEdit || !checkingDescribtionPasswordEdit) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Форма заполенна некорректно((", Toast.LENGTH_LONG);
                    toast.show();
                }
                if (checkingSecondPasswordEdit && checkingFirstPasswordEdit && checkingNickPasswordEdit && checkingDescribtionPasswordEdit) {
                    // вызов другого активити перенести в самый ни
                    Toast toast2 = Toast.makeText(getApplicationContext(),
                            "Регистрация успешно пройдена!", Toast.LENGTH_LONG);
                    toast2.show();
                    saveData();
                    mainCountClientsInt++;
                    mainCountClientsString = String.valueOf(mainCountClientsInt);

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    writeNewUser("0", nickEditString, firstPasswordEditString, describtionEditString);
                            Log.d("APP", "Я зашёл в onCreate()");
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Я зашёл в onCreate()", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                Intent intent = new Intent(ActivityReg.this, MostMainActivity.class);
                startActivity(intent);

                }
                //finish();
                //как сделать так, чтобы на активность регитстрации вообще нельзя было попасть, пока пользователь не нажмёт на кнопку выхода?


        };
        registrationButton = findViewById(R.id.button4);
        registrationButton.setOnClickListener(oclBtnReg);

        View.OnClickListener oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityReg.this, LoginActivity.class);
                startActivity(intent);
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
        // складываешь НИЧЕГО в преференсы, а нужно сохранять логин/пв
        editor.putString(SAVED_LOGIN, nickEditString);
        editor.putString(SAVED_PASSWORD, firstPasswordEditString);
        editor.commit();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
    private void writeNewUser(String userId, String login, String password, String description) {
        User user = new User(userId, login, password, description);
        mDatabase.child("client" + mainCountClientsString).setValue(user);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Зашёл в writeNewUser", Toast.LENGTH_SHORT);
        toast.show();
    }
}
