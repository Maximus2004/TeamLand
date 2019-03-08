package com.example.maxim.myproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    String item;
    String item2;
    Button btn;
    int count = 0;
    int count2 = 0;
    TextView t1, t2, t11, t4, t5, t6, text, t8;
    Button btn2;
    EditText edit;
    int mainCount = 0;
    Button button;
    ScrollView scroll;
    boolean check = true;
    boolean h2, h3 = true;
    EditText name, cani, op1, other, hashs, contact, vkText, othe;
    EditText opisanie;
    boolean itemFlag1, itemFlag2 = false;
    String main = "НЕ ЗАБЫВАЙТЕ ПИСАТЬ ХЭШТЕГИ СЛИТНО. Наприер, 'unityпрограммист'" + "\n";
    boolean words, words2 = true;
    AlertDialog.Builder builder5;
    int pos, pos2, h;
    boolean flag = false;
    boolean f, o, n, u, op = false;
    boolean hash = false;
    boolean mainFlag = true;
    boolean mainFlag2 = true;
    Button btnReady;
    static final int GALLERY_REQUEST = 1;
    String[] cities = {"Сфера программирования:", "Создание игр", "Создание сайтов", "Создание приложений"};
    String[] cities2 = {"Сфера бизнеса (не IT):", "Бизнес в интеренете", "Оффлайн бизнес"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btn2);
        btn2 = (Button) findViewById(R.id.button2);
        btnReady = (Button) findViewById(R.id.button3);

        // создаем обработчик нажатия
        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Как выбрать раздел?")
                        .setMessage("Раздел - это тема вашего будущего проекта. То, для чего вы собираете команду. Например, я собираю команду для создания своего приложения. Значит, я должен раскрыть список под названием \"Сфера программирования\" и выбрать \"Создание приложений\". Если вы неправильно назовёте свой раздел, то не соберёте нужную вам команду, потому что ваша заявка не будет размещена в соответствующем разделе приложения. Если среди списков вы не нашли нужной вам темы, то вы должны одним, двумя или тремя словами (не больше!) выразить тему своей заявки (зачем вы собираете команду?)")
                        .setCancelable(false)
                        .setNegativeButton("Понятно",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
        // присвоим обработчик кнопке OK (btnOk)
        btn.setOnClickListener(oclBtnOk);

        /*name = findViewById(R.id.name);
        opisanie = findViewById(R.id.opisanie);
        scroll = findViewById(R.id.scrollView2);

        View.OnClickListener oclEditOpis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                opisanie.requestFocus();
                scroll.scrollTo(0, 0);
            }
        };
        opisanie.setOnClickListener(oclEditOpis);*/

        /*View.OnTouchListener oclEditName = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                name.requestFocus();
                scroll.scrollTo(0, 0);;
                return true;
            }
        };
        name.setOnTouchListener(oclEditName);*/


        // создаем обработчик нажатия
        View.OnClickListener oclBtnOk3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainCount++;
                edit = findViewById(R.id.editText2);
                name = findViewById(R.id.name);
                hashs = findViewById(R.id.hashTegs);
                opisanie = findViewById(R.id.opisanie);
                cani = findViewById(R.id.can);
                other = findViewById(R.id.editText);
                text = findViewById(R.id.textView);
                othe = findViewById(R.id.otherContact);
                contact = findViewById(R.id.phone);
                vkText = findViewById(R.id.vk);
                op1 = findViewById(R.id.experience);
                String result = edit.getText().toString();
                t1 = findViewById(R.id.textView2);
                t6 = findViewById(R.id.textView6);
                t2 = findViewById(R.id.textView3);
                t11 = findViewById(R.id.textView11);
                t4 = findViewById(R.id.textView4);
                t5 = findViewById(R.id.textView5);
                t8 = findViewById(R.id.textView8);
                int j = 0;
                String resName = name.getText().toString();
                String resOpis = opisanie.getText().toString();
                String resHash = hashs.getText().toString();
                String resVK = vkText.getText().toString();
                String resCont = contact.getText().toString();
                String resOther = othe.getText().toString();
                String resCan = cani.getText().toString();
                String resOpit = op1.getText().toString();
                String resOtherVar = other.getText().toString();
                for (int i = 0; i < result.length(); i++) {
                    if (result.charAt(i) != ' ') {
                        f = true;
                    }
                }
                if (edit.equals("") || !f) {
                    mainFlag = false;
                    mainFlag2 = false;
                    t11.setTextColor(Color.RED);
                    main += "\n" + "Поле 'Цель' не заполнено. Пожалуйста, заполните его (это поможет вам собрать команду)" + "\n";
                } else {
                    t11.setTextColor(Color.BLACK);
                }
                for (int i = 0; i < resCan.length(); i++) {
                    if (resCan.charAt(i) != ' ') {
                        u = true;
                    }
                }
                if (cani.equals("") || !u) {
                    mainFlag = false;
                    mainFlag2 = false;
                    t4.setTextColor(Color.RED);
                    main += "\n" + "Поле 'Навыки, умения' не заполнено. Пожалуйста, заполните его (это поможет вам собрать команду)" + "\n";
                } else {
                    t4.setTextColor(Color.BLACK);
                }
                for (int i = 0; i < resOpis.length(); i++) {
                    if (resOpis.charAt(i) != ' ') {
                        o = true;
                    }
                }
                if (resOpis.equals("") || !o) {
                    mainFlag = false;
                    mainFlag2 = false;
                    t2.setTextColor(Color.RED);
                    main += "\n" + "Поле 'Описание заявки' не заполнено. Пожалуйста, заполните его (это поможет вам собрать команду)" + "\n";
                } else {
                    t2.setTextColor(Color.BLACK);
                }
                for (int i = 0; i < resName.length(); i++) {
                    if (resName.charAt(i) != ' ') {
                        n = true;
                    }
                }
                if (name.equals("") || !n) {
                    mainFlag = false;
                    mainFlag2 = false;
                    t1.setTextColor(Color.RED);
                    main += "\n" + "Поле 'Наименование вашей заявки' или не заполенено (надо обязатльно <- это исправить), или там перечислено сразу несколько специальностей (так лучше не делать, потому что возрастает вероятность того, что вы не соберёте команду)" + "\n";
                } else {
                    t1.setTextColor(Color.BLACK);
                }
                for (int i = 0; i < resOpit.length(); i++) {
                    if (resOpit.charAt(i) != ' ') {
                        op = true;
                    }
                }
                if (resOpis.equals("") || !op) {
                    mainFlag = false;
                    mainFlag2 = false;
                    t5.setTextColor(Color.RED);
                    main += "\n" + "Поле 'Требуемый опыт работы' не заполнено. Пожалуйста, заполните его (это поможет вам собрать команду)" + "\n";
                } else {
                    t5.setTextColor(Color.BLACK);
                } // всё правильно до этого момента
                for (int i = 0; i < resHash.length(); i++) {
                    if (resHash.charAt(i) != ' ') {
                        hash = true;
                    }
                }
                for (int i = 0; i < resHash.length(); i++) {
                    for (char q = 'A'; q <= 'Z'; q++) {
                        if (resHash.charAt(i) == q) {
                            words = false;
                        }
                    }
                }
                for (int i = 0; i < resHash.length(); i++) {
                    for (char q = 'А'; q <= 'Я'; q++) {
                        if (resHash.charAt(i) == q) {
                            words2 = false;
                        }
                    }
                }
                for (int i = 0; i < resHash.length(); i++) {
                    for (char q = '!'; q <= '_'; q++) {
                        if (resHash.charAt(i) == q || resHash.charAt(i) == '>' || resHash.charAt(i) == '<') {
                            h2 = false;
                        }
                    }
                }
                if (resHash.equals("") || !words || !words2 || !hash || !h2) {
                    mainFlag = false;
                    mainFlag2 = false;
                    t6.setTextColor(Color.RED);
                    main += "\n" + "Поле 'Хэштеги' или не заполнено (пожалуйста, заполните его (это поможет вам собрать команду)), или содержит в себе заглавные буквы (все буквы должны быть строчными), или не состоит полностью из строчных букв и пробелов (это поле должно содержать в себе только пробелы или строчные буквы)" + "\n";
                } else {
                    t6.setTextColor(Color.BLACK);
                }
                if (!flag && count == 0) {
                    button.setBackgroundColor(Color.RED);
                    mainFlag = false;
                    check = false;
                    main += "\n"+"Вы не загрузили пример вашей работы (это не обязательно, но благодаря примеру, у будущих сокомандников возрастёт доверие к вам (это поможет вам собрать команду)). Чтобы перейти дальше нажмите 'Понятно, исправлю' и 'Готово'" + "\n";
                } else {
                    button.setBackgroundColor(Color.BLUE);
                }
                for (int i = 0; i < resOtherVar.length(); i++) {
                    if (resOtherVar.charAt(i) == ' ') {
                        j++;
                    }
                }
                if (pos == 0 && pos2 == 0 && resOtherVar.equals("") || j > 2 || pos != 0 && pos2 != 0 || pos != 0 && !resOtherVar.equals("") || pos2 != 0 && !resOtherVar.equals("")) {
                    main += "\n" + "Первая возможная проблема: выберите раздел или напишите свой" + "\n" + "Вторая возможная проблема: в описании своего раздела должно быть не больше трёх слов и между словами должен быть только ОДИН пробел" + "\n" + "Третья возможная проблема: вы выбрали несколько пунктов (нужно выбрать ОДИН)" + "\n";
                    mainFlag = false;
                    mainFlag2 = false;
                    text.setTextColor(Color.RED);
                } else {
                    text.setTextColor(Color.BLACK);
                }
                if (resVK.equals("") && resOther.equals("") && resCont.length() != 11) {
                    mainFlag = false;
                    mainFlag2 = false;
                    main += "\n" + "Номер телефона введён некорректно" + "\n";
                    t8.setTextColor(Color.RED);
                } else {
                    t8.setTextColor(Color.BLACK);
                }
                if (mainFlag || !check && mainCount > 2 && mainFlag2) {
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Заявка успешно опубликована!", Toast.LENGTH_LONG);
                    toast.show();
                }
                if (!mainFlag) {
                    builder5 = new AlertDialog.Builder(MainActivity.this);
                    builder5.setTitle("Ошибка!")
                            .setMessage(main)
                            .setCancelable(false)
                            .setNegativeButton("Понятно, исправлю",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            main = "НЕ ЗАБЫВАЙТЕ ПИСАТЬ ХЭШТЕГИ СЛИТНО. Наприер, 'unityпрограммист'" + "\n";
                                        }
                                    });
                    AlertDialog alert5 = builder5.create();
                    alert5.show();
                    mainFlag = true;
                    mainFlag2 = true;
                    words = true;
                    words2 = true;
                    check = true;
                    h2 = true;
                }
            }
        };
        // присвоим обработчик кнопке OK (btnOk)
        btnReady.setOnClickListener(oclBtnOk3);

        View.OnClickListener oclBtnOk2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                builder2.setTitle("Пример заполнения формы")
                        .setMessage("1. Наименование заявки:" + "\n" + "Unity программисты" + "\n" + "2. Описание заяви:" + "\n" + "Я ищу креативных и умелых Unity программистов для создания игры. Должен быть определённый опыт работы в Unity. Желательно иметь при себе пример своей работы (код и видео ю, где ваша игра работает). Качества лидера (ответственность, креативность, инициативность) приветствуются. \n" +
                                "3. Цель:\n" +
                                "Хочу создать игру. Суть игры объясню только написавшим мне (не хочу выставлять идею напоказ)\n" +
                                "4. Навыки, умения:\n" +
                                "Умелая работа с 3D объектами, работа в команде\n" +
                                "5. Требуемый опыт работы: 0\n" +
                                "6. Хэштеги: команднаяработа естьидея программист unity 3Dобъекты естьопыт креативность инициативность опытныйоснователь\n" +
                                "7. Раздел: Создание игр\n" +
                                "Возможно 'Другой раздел' (например, 'дружба и общение')" + "\n" + "Телефон: 89261231234")
                        .setCancelable(false)
                        .setNegativeButton("Спасибо",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert2 = builder2.create();
                alert2.show();
            }
        };
        // присвоим обработчик кнопке OK (btn)
        btn2.setOnClickListener(oclBtnOk2);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.razdelProgram);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities2);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                item = (String) parent.getItemAtPosition(position);
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
        AdapterView.OnItemSelectedListener itemSelectedListener2 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                item2 = (String) parent.getItemAtPosition(position);
                pos2 = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner2.setOnItemSelectedListener(itemSelectedListener2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("   Формирование заявки");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        flag = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

