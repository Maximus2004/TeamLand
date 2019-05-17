package com.example.maxim.myproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MostMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    String item;
    int pos;
    ImageButton burger;
    ActivityReg reg = new ActivityReg();
    String[] searchFor = {"Поиск по ...", "Хэштегам", "Словам в описаниях"};
    DatabaseReference mDatabase;

    // очень длинный метод, разбить на мелкие
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.most_main_activity);
        ImageButton btn = findViewById(R.id.imageBtn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MostMainActivity.this, CreateApplication.class);
                startActivity(intent);
            }
        };
        btn.setOnClickListener(oclBtnOk);


        Spinner spinner3 = findViewById(R.id.spinner3);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, searchFor);
        // Определяем разметку для использования при выборе элемента
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner3.setAdapter(adapter3);


        AdapterView.OnItemSelectedListener itemSelectedListener3 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                item = (String) parent.getItemAtPosition(position);
                pos = position;
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(19);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner3.setOnItemSelectedListener(itemSelectedListener3);
        spinner3.getBackground().setColorFilter(getResources().getColor(R.color.myColor), PorterDuff.Mode.SRC_ATOP);


        burger = findViewById(R.id.imageButton);
        View.OnClickListener oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        };
        // присвоим обработчик кнопке OK (btnOk)
        burger.setOnClickListener(oclBtn);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        setTitle("TabHost");
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");

        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Бизнес в интеренете");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Оффлайн бизнес");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("Создание игр");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setContent(R.id.tab4);
        tabSpec.setIndicator("Создание сайтов");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag5");
        tabSpec.setContent(R.id.tab5);
        tabSpec.setIndicator("Создание сайтов");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag6");
        tabSpec.setContent(R.id.tab6);
        tabSpec.setIndicator("Создание приложений");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag7");
        tabSpec.setContent(R.id.tab7);
        tabSpec.setIndicator("Другое");
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        MainAdapter adapter = new MainAdapter(this, makeMonth());
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //если раскомментировать строки ниже, то всё так же не работает
                if (view.getId() == R.id.buttonMore) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "" + position, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });
    }

    AdapterElement[] makeMonth() {
        final AdapterElement[][] arr = new AdapterElement[1][1];
        ValueEventListener listenerAtOnce = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "Зашёл в onDataChange", Toast.LENGTH_SHORT).show();
                ArrayList mainNames = new ArrayList();
                ArrayList ambitions = new ArrayList();
                ArrayList experiences = new ArrayList();
                ArrayList examples = new ArrayList();
                ArrayList users = new ArrayList();
                ArrayList applicationIdes = new ArrayList();
                for (int i = 0; i < Integer.parseInt(dataSnapshot.child("applications").child("maxId").getValue().toString()); i++) {
                    if (dataSnapshot.child("applications").child("application" + i + "").getValue() != null) {
                        mainNames.add(dataSnapshot.child("applications").child("application" + i + "").child("name").getValue().toString());
                        ambitions.add(dataSnapshot.child("applications").child("application" + i + "").child("purpose").getValue().toString());
                        experiences.add(dataSnapshot.child("applications").child("application" + i + "").child("experience").getValue().toString());
                        examples.add(dataSnapshot.child("applications").child("application" + i + "").child("example").getValue().toString());
                        users.add(dataSnapshot.child("applications").child("application" + i + "").child("user").getValue().toString());
                        applicationIdes.add(i + "");
                    }
                }
                arr[0] = new AdapterElement[users.size()];
                /*String[] mainName = {"Кулинар", "Программист Unity", "Программист Android Studio", "Надёжный деловой партнёр", "Партнёр по бизнесу", "Друг"};
                String[] ambition = {"Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал..."};
                String[] experience = {"  Опыт: 0", "  Опыт: 6", "  Опыт: 1", "  Опыт: 2", "  Опыт: 3", "  Опыт: 9"};
                String[] exs = {"  Пример работы: нет", "  Пример работы: нет", "  Пример работы: есть", "  Пример работы: нет", "  Пример работы: есть", "  Пример работы: нет"};
                String[] user = {"Maximus", "Vano", "Glebus", "Наталия", "Максим", "Ещё друг"};*/
                // Сборка заявок
                for (int i = 0; i < arr[0].length; i++) {
                    AdapterElement month = new AdapterElement();
                    month.mainName = mainNames.get(i).toString();
                    month.ambition = ambitions.get(i).toString();
                    month.experience = experiences.get(i).toString();
                    month.example = examples.get(i).toString();
                    month.user = users.get(i).toString();
                    month.applicationId = applicationIdes.get(i).toString();
                    arr[0][i] = month;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Зашёл в onCancelled", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
        return arr[0];
    }
    // предыдущий код
    // Метод cоздания массива заявок
    /*AdapterElement[] makeMonth() {
        AdapterElement[] arr = new AdapterElement[6];
        ArrayList mainNames = new ArrayList();
        mainNames.add("Васька");
        for (int i = 0; i <)
            String[] mainName = {"Кулинар", "Программист Unity", "Программист Android Studio", "Надёжный деловой партнёр", "Партнёр по бизнесу", "Друг"};
        String[] ambition = {"Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал...", "Требуется кулинар для помощи в выпечке, расфасовке и продаже хлебо-булочных изделий. Приходите, приходите, приходите! Лалалалалалалалалалал..."};
        String[] experience = {"  Опыт: 0", "  Опыт: 6", "  Опыт: 1", "  Опыт: 2", "  Опыт: 3", "  Опыт: 9"};
        String[] exs = {"  Пример работы: нет", "  Пример работы: нет", "  Пример работы: есть", "  Пример работы: нет", "  Пример работы: есть", "  Пример работы: нет"};
        String[] user = {"Maximus", "Vano", "Glebus", "Наталия", "Максим", "Ещё друг"};
        // Сборка заявок
        for (int i = 0; i < arr.length; i++) {
            AdapterElement month = new AdapterElement();
            month.mainName = mainName[i];
            month.ambition = ambition[i];
            month.experience = experience[i];
            month.example = exs[i];
            month.user = user[i];
            arr[i] = month;
        }
        return arr;
    }*/


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceType")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.favoriets) {
            Intent intent = new Intent(MostMainActivity.this, Chosen.class);
            startActivity(intent);
        } else if (id == R.id.my_applications) {
            Intent intent2 = new Intent(MostMainActivity.this, MyApplications.class);
            startActivity(intent2);
        } else if (id == R.id.changing_describtion) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(MostMainActivity.this);
            builder2.setTitle("Редактирование описания")
                    .setCancelable(false)
                    .setNegativeButton("Отредактировать",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //EditText edit = findViewById(R.id.editText6);
                                    //edit.setText(edit.getText());
                                    dialog.cancel();
                                }
                            });
            LayoutInflater ltInflater = getLayoutInflater();
            View view = ltInflater.inflate(R.layout.dialog_signin, null, false);
            AlertDialog alert2 = builder2.create();
            alert2.setView(view);
            alert2.getWindow().setLayout(265, 130);
            alert2.show();
        } else if (id == R.id.sing_out) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
