package com.example.maxim.myproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ActivityReg extends AppCompatActivity {
    boolean checkingSecondPasswordEdit = true;
    boolean checkingFirstPasswordEdit = true;
    boolean checkingNickPasswordEdit = true;
    boolean checkingDescribtionPasswordEdit = true;
    String firstPasswordEditString, describtionEditString, nickEditString;
    Button registrationButton, authButton;
    EditText theFirstPassword, theSecondPassword, nickEditText, describtion;
    TextView firstPasswordText, secondPasswordText, nickText, describtionText;
    boolean isLoginAlreadyInUse = true;
    String id;
    String loginTemp;
    Map<String,Object> users;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final HashMap<String,User> userMap = new HashMap<>();

        View.OnClickListener oclBtnReg = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSecondPassword();
                checkNicks();
                checkDescription();

                if (!checkingSecondPasswordEdit || !checkingFirstPasswordEdit || !checkingNickPasswordEdit || !checkingDescribtionPasswordEdit) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Форма заполена некорректно", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    ValueEventListener listenerAtOnce = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // сбрасываем флаг
                            isLoginAlreadyInUse = false;
                            Iterable<DataSnapshot> snapshotIterable = dataSnapshot.child("users").getChildren();

                            for (DataSnapshot aSnapshotIterable : snapshotIterable) {
                                //userMap.put(aSnapshotIterable.getKey().toString(), (User) aSnapshotIterable.child("login").getValue());
                                Toast.makeText(getApplicationContext(), aSnapshotIterable.getKey().toString(), Toast.LENGTH_SHORT).show();
                                if (dataSnapshot.child("users").child(aSnapshotIterable.getKey().toString()).child("login").getValue() != null &&
                                        dataSnapshot.child("users").child(aSnapshotIterable.getKey().toString()).child("login").getValue().toString()
                                        .equals(nickEditString)){
                                    isLoginAlreadyInUse = true;
                                }
                            }

                            if (isLoginAlreadyInUse) {
                                Toast.makeText(getApplicationContext(), "Пользователь с таким именем уже зарегистрирован", Toast.LENGTH_LONG).show();
                                nickText.setTextColor(Color.RED);
                            } else {
                                id = UUID.randomUUID().toString();
                                Toast.makeText(getApplicationContext(), "Регистрация успешно пройдена!", Toast.LENGTH_SHORT).show();
                                nickText.setTextColor(Color.BLACK);
                                writeNewUser(nickEditString, firstPasswordEditString, describtionEditString);
                                //mDatabase.child("maxId").setValue(Integer.parseInt(dataSnapshot.child("maxId").getValue().toString()) + 1);
                                Intent intent2 = new Intent(ActivityReg.this, MostMainActivity.class);
                                intent2.putExtra(MostMainActivity.PARAM_USER_ID, id);
                                startActivity(intent2);
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

    private void collectUsersNames(Map<String,Object> users) {
        for (Map.Entry<String, Object> entry : users.entrySet()){
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            loginTemp = (String)singleUser.get("login");
            if (loginTemp != null && !nickEditString.equals("") && loginTemp.equals(nickEditString)){
                isLoginAlreadyInUse = false;
            }
        }
    }

    private void writeNewUser(String login, String password, String description) {
        User user = new User(login, password, description);
        mDatabase.child("users").child(id).setValue(user);
    }

    private void checkSecondPassword() {
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

    private void checkNicks() {
        nickText = findViewById(R.id.textView13);
        nickEditText = findViewById(R.id.editText3);
        nickEditString = nickEditText.getText().toString();
        if (nickEditString.equals("") || nickEditString.length() > 19) {
            nickText.setTextColor(Color.RED);
            checkingNickPasswordEdit = false;
            Toast.makeText(this, "Имя пользователя слишком длинное", Toast.LENGTH_SHORT).show();
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