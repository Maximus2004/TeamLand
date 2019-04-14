package com.example.maxim.myproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityReg extends AppCompatActivity {
    // сделать локальными
    // переназвать, чтобы было понятно
    Button btnReg, btnAvto;
    EditText theFirst, theSecond, nick, sebe;
    TextView first, second, nik, oSebe;
    boolean k = true;
    boolean k2 = true;
    boolean k3 = true;
    boolean k4 = true;
    boolean cont = false;
    SharedPreferences sharedPreferences;
    // должны быть static
    final String SAVED_TEXT = "TEXT";
    final String SAVED_NUM = "NUMBER";
    String oSebeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // никогда не вызывается
        if (cont) {
            Intent intent = new Intent(ActivityReg.this, Main2Activity.class);
            startActivity(intent);
        }

        // нечитабельно, разбить метод на несколько мелких
        //не забудь про проверку на совпадение ников
        View.OnClickListener oclBtnReg = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theFirst = findViewById(R.id.editText4);
                theSecond = findViewById(R.id.editText5);
                first = findViewById(R.id.textView14);
                second = findViewById(R.id.textView15);
                nik = findViewById(R.id.textView13);
                nick = findViewById(R.id.editText3);
                oSebe = findViewById(R.id.textView16);
                sebe = findViewById(R.id.editText7);
                String nickEdit = nick.getText().toString();
                oSebeEdit = sebe.getText().toString();
                String firstEdit = theFirst.getText().toString();
                String secondEdit = theSecond.getText().toString();

                // можно сделать один if
                if (secondEdit.equals("") || !secondEdit.equals(firstEdit)) {
                    second.setTextColor(Color.RED);
                    k = false;
                }
                if (secondEdit.equals(firstEdit)) {
                    second.setTextColor(Color.BLACK);
                    k = true;
                }
                if (firstEdit.equals("")) {
                    first.setTextColor(Color.RED);
                    k2 = false;
                } else {
                    first.setTextColor(Color.BLACK);
                    k2 = true;
                }
                if (nickEdit.equals("")) {
                    nik.setTextColor(Color.RED);
                    k3 = false;
                } else {
                    nik.setTextColor(Color.BLACK);
                    k3 = true;
                }
                if (oSebeEdit.equals("")) {
                    oSebe.setTextColor(Color.RED);
                    k4 = false;
                } else {
                    oSebe.setTextColor(Color.BLACK);
                    k4 = true;
                }

                // сделать одним if
                if (!k || !k2 || !k3 || !k4) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Форма заполенна некорректно((", Toast.LENGTH_LONG);
                    toast.show();
                }
                if (k && k2 && k3 && k4) {
                    // вызов другого активити перенести в самый низ
                    Intent intent = new Intent(ActivityReg.this, MostMainActivity.class);
                    startActivity(intent);
                    Toast toast2 = Toast.makeText(getApplicationContext(),
                            "Регистрация успешно пройдена!", Toast.LENGTH_LONG);
                    toast2.show();
                    saveData();
                    EditText edit = findViewById(R.id.editText6);
                    edit.setText(oSebe.getText());                       
                    cont = true;
                    // в конце надо вызывать finish() – активити убьется
                    // и сюда нельзя будет вернуться кнопкой назад
                }
            }
        };
        btnReg = findViewById(R.id.button4);
        btnReg.setOnClickListener(oclBtnReg);

        View.OnClickListener oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityReg.this, LoginActivity.class);
                startActivity(intent);
            }
        };
        btnAvto = findViewById(R.id.button5);
        btnAvto.setOnClickListener(oclBtn);
    }

    void saveData() {
        // преференсы можно сделать локальные
        // текущий вызов достает преференсы ТОЛЬКО для активити
        // чтобы они были для всего приложения нужно вызывать getPreferences("текст1", MODE_PRIVATE)
        // и в другом активити вызывать также getPreferences("текст1", MODE_PRIVATE)
        // вместо "текст1" может быть любой ключ, но одинаковый в обоих вызовах
        // SAVED_TEXT и SAVED_NUM должны быть static и их можно будет вызывать так: ActivityReg.SAVED_TEXT
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // складываешь НИЧЕГО в преференсы, а нужно сохранять логин/пв
        editor.putString(SAVED_TEXT, "");
        editor.putString(SAVED_NUM, "");
        editor.commit();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
}
