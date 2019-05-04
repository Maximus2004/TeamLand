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
    // должны быть static
    static final String SAVED_LOGIN = "LOGIN";
    static final String SAVED_PASSWORD = "PASSWORD";
    // не получается сделать локальными, потому что нельзя, чтобы были final
    boolean checkingSecondPasswordEdit = true;
    boolean checkingFirstPasswordEdit = true;
    boolean checkingNickPasswordEdit = true;
    boolean checkingDescribtionPasswordEdit = true;
    String nickEditString;
    String firstPasswordEditString;
    String describtionEditString;
    Button registrationButton, authButton;
    EditText theFirstPassword, theSecondPassword, nickEditText, describtion;
    TextView firstPasswordText, secondPasswordText, nickText, describtionText;
    String mainCountClientsString;
    int mainCountClientsInt = 0;
    boolean isLoginAlreadyInUse = true;
    String dataSnapshot2 = "0";
    private DatabaseReference mDatabase;

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
                            "Форма заполенна некорректно", Toast.LENGTH_LONG);
                    toast.show();
                } else { // а если так? и я передвинул закрывающую скобку в нужное место
                    Toast toast2 = Toast.makeText(getApplicationContext(),
                            "Регистрация успешно пройдена!", Toast.LENGTH_LONG);
                    toast2.show();
                    saveData();
                    // зачем эти два поля?
                    mainCountClientsInt++;
                    mainCountClientsString = String.valueOf(mainCountClientsInt);
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    ValueEventListener listenerAtOnce = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Toast.makeText(getApplicationContext(), "onDataChange() в listenerAtOnce", Toast.LENGTH_SHORT).show();
                            // сбрасываем флаг
                            isLoginAlreadyInUse = false;
                            // так более читабельное
                            int maxId = Integer.parseInt(dataSnapshot.child("maxId").getValue().toString());
                            for (int i = 0; i < maxId; i++) {    //i < id
                                // можно не приводить i к String, в таком виде тоже сработает
                                Object entity = dataSnapshot.child("client" + i).child("login").getValue();
                                // entity может быть NULL
                                // потому что из-за разных тестов maxId стал больше, чем есть записей
                                if (entity != null && entity.toString().equals(nickEditString)) {
                                    // нашли похожий, останавливаем цикл
                                    isLoginAlreadyInUse = true;
                                    break;
                                }
                            }

                            if (isLoginAlreadyInUse) {
                                Toast.makeText(getApplicationContext(), "Ники совпали", Toast.LENGTH_SHORT).show();

                                // не вижу смысл этого условия – выше его уже проверили и тупо сюда не попадем
                                // по хорошему ник надо проверять там же, где ввод проверяется, а не тут
//                            } else if (checkingSecondPasswordEdit && checkingFirstPasswordEdit && checkingNickPasswordEdit && checkingDescribtionPasswordEdit) {
                            } else {
                                Toast.makeText(getApplicationContext(), "Ники не совпали и всё нормусь", Toast.LENGTH_SHORT).show();
                                mainCountClientsInt++;
                                mainCountClientsString = String.valueOf(mainCountClientsInt);
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                // очень странная логика с dataSnapshot2
                                dataSnapshot2 = dataSnapshot.child("maxId").getValue().toString();
                                writeNewUser(dataSnapshot.child("maxId").getValue().toString(), nickEditString, firstPasswordEditString, describtionEditString);
                                mDatabase.child("maxId").setValue(Integer.parseInt(dataSnapshot.child("maxId").getValue().toString()) + 1);
                                Intent intent = new Intent(ActivityReg.this, MostMainActivity.class);
                                startActivity(intent);
                                // финишируем активити при успешной регистрации
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Я зашёл в onCancelled()", Toast.LENGTH_SHORT);
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
                // финишируем активити при переходе на логин
                // но вернуться на него можно будет только после перезапуска приложения
                // наверное, нужна кнопка перехода на регистрацию с LoginACtivity? :)
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
        // складываешь НИЧЕГО в преференсы, а нужно сохранять логин/пв
        editor.putString(SAVED_LOGIN, nickEditString);
        editor.putString(SAVED_PASSWORD, firstPasswordEditString);
        editor.commit();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    private void writeNewUser(String userId, String login, String password, String description) {
        User user = new User(userId, login, password, description);
        mDatabase.child("client" + dataSnapshot2);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Зашёл в writeNewUser", Toast.LENGTH_SHORT);
        toast.show();
    }
}
