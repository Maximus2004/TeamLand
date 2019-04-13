package com.example.maxim.myproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity{
    String[] cities3 = {"Поиск по ...", "Хэштегам", "Словам в описаниях"};
    String item;
    int pos;
    ImageButton burger;
    String[] mCatTitles = {"Избранные", "Мои заявки", "Редактирование описания себя", "Выход"};
    public DrawerLayout mDrawerListView;
    public ListView mDrawerListView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ImageButton btn = findViewById(R.id.imageBtn);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, CreateApplication.class);
                startActivity(intent);
            }
        };
        // присвоим обработчик кнопке OK (btnOk)
        btn.setOnClickListener(oclBtnOk);

        Spinner spinner3 = findViewById(R.id.spinner3);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities3);
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
        mDrawerListView = findViewById(R.id.drawer_layout);
        mDrawerListView2 = findViewById(R.id.left_drawer);
        // подключим адаптер для списка
        mDrawerListView2.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mCatTitles));
        View.OnClickListener oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerListView.openDrawer(GravityCompat.START);
            }
        };
        // присвоим обработчик кнопке OK (btnOk)
        burger.setOnClickListener(oclBtn);
        // установим слушатель для щелчков по элементам списка
        mDrawerListView2.setOnItemClickListener(new DrawerItemClickListener());
    }
    public void toast(int position){
        if (position == 1){
            Intent intent = new Intent(Main2Activity.this, Chosen.class);
            startActivity(intent);
        }
    }
}
