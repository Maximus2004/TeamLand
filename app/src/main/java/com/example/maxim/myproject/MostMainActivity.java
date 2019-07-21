package com.example.maxim.myproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MostMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainAdapter.UserActionListener, MainAdapterForOther.UserActionListener {
    public static final String TAG = "MostMainActivity";
    public static final String PARAM_USER_NAME = TAG + ".username";

    String item;
    String userName = null;
    int pos;
    EditText searchEditText;
    AlertDialog.Builder builderHashtegs;
    ImageButton burger;
    String[] searchFor = {"Поиск по ...", "Хэштегам", "Словам в описаниях"};
    DatabaseReference mDatabase;
    EditText edit;
    ListView lv, lv2, lv3, lv4, lv5, lv6, lv7;
    boolean controlBurger = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.most_main_activity);

        // весь код, что был здесь разделил на методы и чуть отредактировал
        // так удобно читать и понимать код – каждый метод занимается только какой-то одной "задачей"
        // и в том числе может быть разделен еще на методы

        // достаем информацию, переданную из LoginActivity, если она есть
        // если такого параметра нет, то будет null
        Intent intent = getIntent();
        userName = intent.getStringExtra(PARAM_USER_NAME);

        // инициализируем все, что требуется для работы
        init();
    }

    private void init() {
        // общие инциализации и настройки
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // настройки тулбара, навигации и отображения
        setupNavigation();
        setupTabs();
        setupTitle();
        setupLists();

        // заполняем списки информацией
        fillData();

        // настройка поиска
        setupSearch();

        // кнопка "Создать новую заявку"
        setupCreateAppButton();
    }

    private void setupTitle() {
        ValueEventListener listenerAtOnceUserName = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (userName != null) {
                    TextView headerView = findViewById(R.id.headerTextView);
                    headerView.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(listenerAtOnceUserName);
    }

    private void setupLists() {
        lv = findViewById(R.id.listView);
        lv2 = findViewById(R.id.listView2);
        lv3 = findViewById(R.id.listView3);
        lv4 = findViewById(R.id.listView4);
        lv5 = findViewById(R.id.listView5);
        lv6 = findViewById(R.id.listView6);
        lv7 = findViewById(R.id.listView7);

        /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //если раскомментировать строки ниже, то всё так же не работает
                //if (view.getId() == R.id.buttonMore) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "" + position, Toast.LENGTH_SHORT);
                toast.show();
                //}
            }

        });*/
    }

    private void setupNavigation() {
        // настройка тулбара
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // настройка навигации в приложении
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        burger = findViewById(R.id.imageButton);
        View.OnClickListener oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlBurger)
                    drawer.openDrawer(GravityCompat.START);
                else {
                    fillData();
                    controlBurger = true;
                    burger.setImageResource(R.drawable.huray);
                }
            }
        };
        // присвоим обработчик кнопке OK (btnOk)
        burger.setOnClickListener(oclBtn);
    }

    private void setupCreateAppButton() {
        View.OnClickListener onCreateAppClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MostMainActivity.this, CreateApplication.class);
                intent.putExtra(CreateApplication.PARAM_USER_NAME, userName);
                startActivity(intent);
            }
        };

        ImageButton createAppButton = findViewById(R.id.imageBtn);
        createAppButton.setOnClickListener(onCreateAppClickListener);
    }

    private void setupSearch() {
        searchEditText = findViewById(R.id.searchEditText);

        ImageButton btnSearch = findViewById(R.id.btnSearch);
        View.OnClickListener onSearchButtonClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchEditText.getText().toString().toLowerCase();

                if (searchText != null && searchText != "" && !searchText.equals("")) {
                    if (pos == 0) {
                        // тип поиска не выбран, просим выбрать тип поиска
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Выберите критерий поиска (чёрный треугольник справа)", Toast.LENGTH_LONG);
                        toast.show();
                    } else if (pos == 1) {
                        // выбран поиск по хеш тегам
                        builderHashtegs = new AlertDialog.Builder(MostMainActivity.this);
                        builderHashtegs.setTitle("Поиск по хэштегам")
                                .setMessage("Осуществляя поиск по хэштегам указывайте их через пробел без использования посторонних символов." + "\n" + "Например, если " +
                                        "вам надо упомянуть в стороке поиска следующие хэштеги: приложения, создание приложения, опытный специалист, программист, " +
                                        "то вам следует указать эти хэштеги в слудующем виде: приложения созданиеприложения опытныйспециалист программист. Без " +
                                        "специальных символов. ")
                                .setCancelable(false)
                                .setNegativeButton("Понятно",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        Toast.makeText(getApplicationContext(), "Зашёл в первое условие", Toast.LENGTH_SHORT).show();

                        AlertDialog alertHashtegs = builderHashtegs.create();
                        alertHashtegs.show();
                        burger.setImageResource(R.drawable.back2);
                        controlBurger = false;

                        makeApplicationForSearchByHashtegs(searchText);
                    } else {
                        // выбран поиск по словам
                        makeApplicationForSearchByWords(searchText);
                        burger.setImageResource(R.drawable.back2);
                        controlBurger = false;
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "Заполните строку поиска", Toast.LENGTH_SHORT).show();
            }
        };
        btnSearch.setOnClickListener(onSearchButtonClicked);

        Spinner spinner3 = findViewById(R.id.spinner3);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, searchFor);
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
    }

    private void makeApplicationForSearchByHashtegs(final String searchText) {
        Toast.makeText(getApplicationContext(), "Зашёл в функцию byHashtegs", Toast.LENGTH_SHORT).show();

        // делим поисковый запрос на слова
        final String[] searchWords = searchText.split(" ");

        final AdapterElement[][] arrBuisness = new AdapterElement[1][];
        final AdapterElement[][] arrAll = new AdapterElement[1][];
        final AdapterElement[][] arrInternet = new AdapterElement[1][];
        final AdapterElement[][] arrGames = new AdapterElement[1][];
        final AdapterElement[][] arrSites = new AdapterElement[1][];
        final AdapterElement[][] arrApps = new AdapterElement[1][];
        final AdapterElementOther[][] arrOther = new AdapterElementOther[1][1];
        ValueEventListener listenerAtOnce = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "Зашёл в onDataChange", Toast.LENGTH_SHORT).show();

                ArrayList<String> internetMainNames = new ArrayList<>();
                ArrayList<String> internetAmbitions = new ArrayList<>();
                ArrayList<String> internetExperience = new ArrayList<>();
                ArrayList<String> internetExamples = new ArrayList<>();
                ArrayList<String> internetUsers = new ArrayList<>();
                ArrayList<Integer> internetApplicationIds = new ArrayList<>();

                ArrayList<String> buisnessMainNames = new ArrayList<>();
                ArrayList<String> buisnessAmbitions = new ArrayList<>();
                ArrayList<String> buisnessExperience = new ArrayList<>();
                ArrayList<String> buisnessExamples = new ArrayList<>();
                ArrayList<String> buisnessUsers = new ArrayList<>();
                ArrayList<Integer> buisnessApplicationIds = new ArrayList<>();

                ArrayList<String> gamesMainNames = new ArrayList<>();
                ArrayList<String> gamesAbmitions = new ArrayList<>();
                ArrayList<String> gamesExperience = new ArrayList<>();
                ArrayList<String> gamesExamples = new ArrayList<>();
                ArrayList<String> gamesUsers = new ArrayList<>();
                ArrayList<Integer> gamesApplicationIds = new ArrayList<>();

                ArrayList<String> sitesMainNames = new ArrayList<>();
                ArrayList<String> sitesAmbitions = new ArrayList<>();
                ArrayList<String> sitesExperience = new ArrayList<>();
                ArrayList<String> sitesExamples = new ArrayList<>();
                ArrayList<String> sitesUsers = new ArrayList<>();
                ArrayList<Integer> sitesApplicationIds = new ArrayList<>();

                ArrayList<String> appsMainNames = new ArrayList<>();
                ArrayList<String> appsAmbitions = new ArrayList<>();
                ArrayList<String> appsExperiens = new ArrayList<>();
                ArrayList<String> appsExamples = new ArrayList<>();
                ArrayList<String> appsUsers = new ArrayList<>();
                ArrayList<Integer> appsApplicationIds = new ArrayList<>();

                ArrayList<String> otherMainNames = new ArrayList<>();
                ArrayList<String> otherAmbitions = new ArrayList<>();
                ArrayList<String> otherExperience = new ArrayList<>();
                ArrayList<String> otherExamples = new ArrayList<>();
                ArrayList<String> otherUsers = new ArrayList<>();
                ArrayList<Integer> otherApplicationIds = new ArrayList<>();
                ArrayList<String> otherSection = new ArrayList();

                ArrayList<String> allMainNames = new ArrayList<>();
                ArrayList<String> allAmbitions = new ArrayList<>();
                ArrayList<String> allExperience = new ArrayList<>();
                ArrayList<String> allExamples = new ArrayList<>();
                ArrayList<String> allUsers = new ArrayList<>();
                ArrayList<Integer> allApplicationIds = new ArrayList<>();

                String bigName, name, section, bigSection;

                // сделаем treeMap, который по умолчанию отсортирован, но так как нужн обратный порядок
                // то передаем в конструкторе нужный порядок сортировки
                TreeMap<Integer, List<Integer>> mapCountToIds = new TreeMap<>(Collections.reverseOrder());

                DataSnapshot appTable = dataSnapshot.child("applications");
                int maxId = Integer.parseInt(appTable.child("maxId").getValue().toString());

                // подсчитываем количество найденых тегов для заявок
                for (int appIndex = 0; appIndex < maxId; appIndex++) {
                    // берем все теги заявки в исходном виде
                    DataSnapshot appTagsRaw = appTable.child("application" + appIndex).child("hashs");
                    if (appTagsRaw.getValue() != null) {
                        // разделяем их на отдельные слова по пробелу
                        List<String> appTags = Arrays
                                .asList(appTagsRaw.getValue().toString()
                                        .split(" ")
                                );

                        // подсчитываем сколько из них подходит под поисковый запрос
                        int foundTagsCount = 0;
                        for (String word : searchWords) {
                            if (appTags.contains(word)) foundTagsCount++;
                        }

                        // сохраняем в мапу
                        // value как список, так как у разных заявок может быть одинаковое количество совпадений
                        if (foundTagsCount > 0) {
                            List<Integer> ids;
                            // смотрим, есть ли уже такое количество совпадений
                            // если есть, то добавляем в существующий список
                            if (mapCountToIds.containsKey(foundTagsCount))
                                ids = mapCountToIds.get(foundTagsCount);
                            else
                                ids = new ArrayList<>();

                            ids.add(appIndex);
                            mapCountToIds.put(foundTagsCount, ids);
                        }
                    }
                }
                // вот тут ты делал новую сущность (в mas.toArray()) и не использовал ее, то есть она сортировалась, а mas так и оставался не отсоритрованным
//                Arrays.sort(mas.toArray(), Collections.reverseOrder()); // сортируем массив по убыванию

                // до этого момента всё верно

                // так как мы взяли TreeMap, то там все уже отсортировано по убыванию
                // поэтому просто прогоняем цикл по всем ключам и значениям
                if (!mapCountToIds.isEmpty()) {
                    for (Map.Entry<Integer, List<Integer>> entry : mapCountToIds.entrySet()) {
                        for (int appId : entry.getValue()) {
                            DataSnapshot app = appTable.child("application" + appId);
                            DataSnapshot appSection = app.child("section");

                            // если почему-то нет такой записи или у нее нет section (какая-то ошибка), то пропускаем этот id
                            if (app.getValue() == null || appSection.getValue() == null) continue;

                            // чуть-чуть порядка :)
                            DataSnapshot appName = app.child("name");
                            DataSnapshot appPurpose = app.child("purpose");
                            DataSnapshot appExp = app.child("experience");
                            DataSnapshot appExample = app.child("example");
                            DataSnapshot appCreator = app.child("creator");

                            // если выше условие прошло, то это полюбому будет добавлено, поэтому вынес наверх
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                allMainNames.add("  " + bigName + "...");
                            } else {
                                allMainNames.add("  " + appName.getValue().toString());
                            }
                            if (appPurpose.getValue().toString().length() > 146) {
                                bigName = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigName += name.charAt(j);
                                }
                                allAmbitions.add(bigName + "...");
                            } else {
                                allAmbitions.add(appPurpose.getValue().toString());
                            }
                            allExperience.add("  Опыт: " + appExp.getValue().toString());
                            allExamples.add("  Пример работы: " + appExample.getValue().toString());
                            allUsers.add(appCreator.getValue().toString());
                            allApplicationIds.add(appId);

                            // обработка по секциям
                            if (appSection.getValue().toString().equals("Бизнес в интернете")) {
                                if (appName.getValue().toString().length() > 25) {
                                    bigName = "";
                                    name = appName.getValue().toString();
                                    for (int j = 0; j < 25; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    internetMainNames.add("  " + bigName + "...");
                                } else {
                                    internetMainNames.add("  " + appName.getValue().toString());
                                }
                                if (appPurpose.getValue().toString().length() > 146) {
                                    bigName = "";
                                    name = appPurpose.getValue().toString();
                                    for (int j = 0; j < 146; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    internetAmbitions.add(bigName + "...");
                                } else {
                                    internetAmbitions.add(appPurpose.getValue().toString());
                                }
                                internetExperience.add("  Опыт: " + appExp.getValue().toString());
                                internetExamples.add("  Пример работы: " + appExample.getValue().toString());
                                internetUsers.add(appCreator.getValue().toString());
                                internetApplicationIds.add(appId);
                            } else if (appSection.getValue() != null && appSection.getValue().equals("Оффлайн бизнес")) {
                                if (appName.getValue().toString().length() > 25) {
                                    bigName = "";
                                    name = appName.getValue().toString();
                                    for (int j = 0; j < 25; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    buisnessMainNames.add("  " + bigName + "...");
                                } else {
                                    buisnessMainNames.add("  " + appName.getValue().toString());
                                }
                                if (appPurpose.getValue().toString().length() > 146) {
                                    bigName = "";
                                    name = appPurpose.getValue().toString();
                                    for (int j = 0; j < 146; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    buisnessAmbitions.add(bigName + "...");
                                } else {
                                    buisnessAmbitions.add(appPurpose.getValue().toString());
                                }
                                buisnessExperience.add("  Опыт: " + appExp.getValue().toString());
                                buisnessExamples.add("  Пример работы: " + appExample.getValue().toString());
                                buisnessUsers.add(appCreator.getValue().toString());
                                buisnessApplicationIds.add(appId);
                            } else if (appSection.getValue() != null && appSection.getValue().equals("Создание игр")) {
                                if (appName.getValue().toString().length() > 25) {
                                    bigName = "";
                                    name = appName.getValue().toString();
                                    for (int j = 0; j < 25; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    gamesMainNames.add("  " + bigName + "...");
                                } else {
                                    gamesMainNames.add("  " + appName.getValue().toString());
                                }
                                if (appPurpose.getValue().toString().length() > 146) {
                                    bigName = "";
                                    name = appPurpose.getValue().toString();
                                    for (int j = 0; j < 146; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    gamesAbmitions.add(bigName + "...");
                                } else {
                                    gamesAbmitions.add(appPurpose.getValue().toString());
                                }
                                gamesExperience.add("  Опыт: " + appExp.getValue().toString());
                                gamesExamples.add("  Пример работы: " + appExample.getValue().toString());
                                gamesUsers.add(appCreator.getValue().toString());
                                gamesApplicationIds.add(appId);
                            } else if (appSection.getValue() != null && appSection.getValue().equals("Создание сайтов")) {
                                if (appName.getValue().toString().length() > 25) {
                                    bigName = "";
                                    name = appName.getValue().toString();
                                    for (int j = 0; j < 25; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    sitesMainNames.add("  " + bigName + "...");
                                } else {
                                    sitesMainNames.add("  " + appName.getValue().toString());
                                }
                                if (appPurpose.getValue().toString().length() > 146) {
                                    bigName = "";
                                    name = appPurpose.getValue().toString();
                                    for (int j = 0; j < 146; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    sitesAmbitions.add(bigName + "...");
                                } else {
                                    sitesAmbitions.add(appPurpose.getValue().toString());
                                }
                                sitesExperience.add("  Опыт: " + appExp.getValue().toString());
                                sitesExamples.add("  Пример работы: " + appExample.getValue().toString());
                                sitesUsers.add(appCreator.getValue().toString());
                                sitesApplicationIds.add(appId);
                            } else if (appSection.getValue() != null && appSection.getValue().equals("Создание приложений")) {
                                if (appName.getValue().toString().length() > 25) {
                                    bigName = "";
                                    name = appName.getValue().toString();
                                    for (int j = 0; j < 25; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    appsMainNames.add("  " + bigName + "...");
                                } else {
                                    appsMainNames.add("  " + appName.getValue().toString());
                                }
                                if (appPurpose.getValue().toString().length() > 146) {
                                    bigName = "";
                                    name = appPurpose.getValue().toString();
                                    for (int j = 0; j < 146; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    appsAmbitions.add(bigName + "...");
                                } else {
                                    appsAmbitions.add(appPurpose.getValue().toString());
                                }
                                appsExperiens.add("  Опыт: " + appExp.getValue().toString());
                                appsExamples.add("  Пример работы: " + appExample.getValue().toString());
                                appsUsers.add(appCreator.getValue().toString());
                                appsApplicationIds.add(appId);
                            } else {
                                if (appName.getValue().toString().length() > 25) {
                                    bigName = "";
                                    name = appName.getValue().toString();
                                    for (int j = 0; j < 25; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    otherMainNames.add("  " + bigName + "...");
                                } else {
                                    otherMainNames.add("  " + appName.getValue().toString());
                                }
                                if (appPurpose.getValue().toString().length() > 146) {
                                    bigName = "";
                                    name = appPurpose.getValue().toString();
                                    for (int j = 0; j < 146; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    otherAmbitions.add(bigName + "...");
                                } else {
                                    otherAmbitions.add(appPurpose.getValue().toString());
                                }
                                otherExperience.add("  Опыт: " + appExp.getValue().toString());
                                otherExamples.add("  Пример работы: " + appExample.getValue().toString());
                                otherUsers.add(appCreator.getValue().toString());
                                if (appSection.getValue().toString().length() > 21) {
                                    bigSection = "";
                                    section = appSection.getValue().toString();
                                    for (int j = 0; j < 21; j++) {
                                        bigSection += section.charAt(j);
                                    }
                                    otherSection.add("  Раздел:  " + bigSection + "...");
                                } else {
                                    otherSection.add("  Раздел:  " + appSection.getValue().toString());
                                }
                                otherApplicationIds.add(appId);
                            }
                        }

                        arrAll[0] = new AdapterElement[allMainNames.size()];
                        arrInternet[0] = new AdapterElement[internetMainNames.size()];
                        arrBuisness[0] = new AdapterElement[buisnessMainNames.size()];
                        arrGames[0] = new AdapterElement[gamesMainNames.size()];
                        arrSites[0] = new AdapterElement[sitesMainNames.size()];
                        arrApps[0] = new AdapterElement[appsMainNames.size()];
                        arrOther[0] = new AdapterElementOther[otherMainNames.size()];
                    }
                    for (int i = 0; i < arrAll[0].length; i++) {
                        AdapterElement month = new AdapterElement();
                        month.mainName = allMainNames.get(i);
                        month.ambition = allAmbitions.get(i);
                        month.experience = allExperience.get(i);
                        month.example = allExamples.get(i);
                        month.user = allUsers.get(i);
                        month.applicationId = allApplicationIds.get(i).toString();
                        arrAll[0][i] = month;
                    }
                    for (int i = 0; i < arrBuisness[0].length; i++) {
                        AdapterElement month = new AdapterElement();
                        month.mainName = buisnessMainNames.get(i);
                        month.ambition = buisnessAmbitions.get(i);
                        month.experience = buisnessExperience.get(i);
                        month.example = buisnessExamples.get(i);
                        month.user = buisnessUsers.get(i);
                        month.applicationId = buisnessApplicationIds.get(i).toString();
                        arrBuisness[0][i] = month;
                    }
                    for (int i = 0; i < arrGames[0].length; i++) {
                        AdapterElement month = new AdapterElement();
                        month.mainName = gamesMainNames.get(i);
                        month.ambition = gamesAbmitions.get(i);
                        month.experience = gamesExperience.get(i);
                        month.example = gamesExamples.get(i);
                        month.user = gamesUsers.get(i);
                        month.applicationId = gamesApplicationIds.get(i).toString();
                        arrGames[0][i] = month;
                    }
                    for (int i = 0; i < arrSites[0].length; i++) {
                        AdapterElement month = new AdapterElement();
                        month.mainName = sitesMainNames.get(i);
                        month.ambition = sitesAmbitions.get(i);
                        month.experience = sitesExperience.get(i);
                        month.example = sitesExamples.get(i);
                        month.user = sitesUsers.get(i);
                        month.applicationId = sitesApplicationIds.get(i).toString();
                        arrSites[0][i] = month;
                    }
                    for (int i = 0; i < arrInternet[0].length; i++) {
                        AdapterElement month = new AdapterElement();
                        month.mainName = internetMainNames.get(i);
                        month.ambition = internetAmbitions.get(i);
                        month.experience = internetExperience.get(i);
                        month.example = internetExamples.get(i);
                        month.user = internetUsers.get(i);
                        month.applicationId = internetApplicationIds.get(i).toString();
                        arrInternet[0][i] = month;
                    }
                    for (int i = 0; i < arrApps[0].length; i++) {
                        AdapterElement month = new AdapterElement();
                        month.mainName = appsMainNames.get(i);
                        month.ambition = appsAmbitions.get(i);
                        month.experience = appsExperiens.get(i);
                        month.example = appsExamples.get(i);
                        month.user = appsUsers.get(i);
                        month.applicationId = appsApplicationIds.get(i).toString();
                        arrApps[0][i] = month;
                    }
                    for (int i = 0; i < arrOther[0].length; i++) {
                        AdapterElementOther month = new AdapterElementOther();
                        month.mainName = otherMainNames.get(i);
                        month.ambition = otherAmbitions.get(i);
                        month.experience = otherExperience.get(i);
                        month.example = otherExamples.get(i);
                        month.user = otherUsers.get(i);
                        month.applicationId = otherApplicationIds.get(i).toString();
                        month.sectionClass = otherSection.get(i).toString();
                        arrOther[0][i] = month;
                    }

                    MainAdapter adapterAll = new MainAdapter(MostMainActivity.this, arrAll[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterAll.setUserActionListener(MostMainActivity.this);
                    lv.setAdapter(adapterAll);
                    MainAdapter adapterBuisness = new MainAdapter(MostMainActivity.this, arrBuisness[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterBuisness.setUserActionListener(MostMainActivity.this);
                    lv2.setAdapter(adapterBuisness);
                    MainAdapter adapterGames = new MainAdapter(MostMainActivity.this, arrGames[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterGames.setUserActionListener(MostMainActivity.this);
                    lv3.setAdapter(adapterGames);
                    MainAdapter adapterSites = new MainAdapter(MostMainActivity.this, arrSites[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterSites.setUserActionListener(MostMainActivity.this);
                    lv4.setAdapter(adapterSites);
                    MainAdapter adapterInternet = new MainAdapter(MostMainActivity.this, arrInternet[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterInternet.setUserActionListener(MostMainActivity.this);
                    lv5.setAdapter(adapterInternet);
                    MainAdapter adapterApps = new MainAdapter(MostMainActivity.this, arrApps[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterApps.setUserActionListener(MostMainActivity.this);
                    lv6.setAdapter(adapterApps);
                    MainAdapterForOther adapterOther = new MainAdapterForOther(MostMainActivity.this, arrOther[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterOther.setUserActionListener(MostMainActivity.this);
                    lv7.setAdapter(adapterOther);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Ничего не найдено", Toast.LENGTH_LONG).show();
                    lv.removeAllViewsInLayout();
                    lv2.removeAllViewsInLayout();
                    lv3.removeAllViewsInLayout();
                    lv4.removeAllViewsInLayout();
                    lv5.removeAllViewsInLayout();
                    lv6.removeAllViewsInLayout();
                    lv7.removeAllViewsInLayout();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Зашёл в onCancelled", Toast.LENGTH_SHORT).show();
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
    }

    private void makeApplicationForSearchByWords(final String searchText) {
        final String[] searchWords = searchText.split(" ");

        final AdapterElement[][] arrBuisness = new AdapterElement[1][];
        final AdapterElement[][] arrAll = new AdapterElement[1][];
        final AdapterElement[][] arrInternet = new AdapterElement[1][];
        final AdapterElement[][] arrGames = new AdapterElement[1][];
        final AdapterElement[][] arrSites = new AdapterElement[1][];
        final AdapterElement[][] arrApps = new AdapterElement[1][];
        final AdapterElementOther[][] arrOther = new AdapterElementOther[1][1];
        ValueEventListener listenerAtOnce = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "Зашёл в onDataChange", Toast.LENGTH_SHORT).show();

                ArrayList<String> internetMainNames = new ArrayList<>();
                ArrayList<String> internetAmbitions = new ArrayList<>();
                ArrayList<String> internetExperience = new ArrayList<>();
                ArrayList<String> internetExamples = new ArrayList<>();
                ArrayList<String> internetUsers = new ArrayList<>();
                ArrayList<Integer> internetApplicationIds = new ArrayList<>();

                ArrayList<String> buisnessMainNames = new ArrayList<>();
                ArrayList<String> buisnessAmbitions = new ArrayList<>();
                ArrayList<String> buisnessExperience = new ArrayList<>();
                ArrayList<String> buisnessExamples = new ArrayList<>();
                ArrayList<String> buisnessUsers = new ArrayList<>();
                ArrayList<Integer> buisnessApplicationIds = new ArrayList<>();

                ArrayList<String> gamesMainNames = new ArrayList<>();
                ArrayList<String> gamesAbmitions = new ArrayList<>();
                ArrayList<String> gamesExperience = new ArrayList<>();
                ArrayList<String> gamesExamples = new ArrayList<>();
                ArrayList<String> gamesUsers = new ArrayList<>();
                ArrayList<Integer> gamesApplicationIds = new ArrayList<>();

                ArrayList<String> sitesMainNames = new ArrayList<>();
                ArrayList<String> sitesAmbitions = new ArrayList<>();
                ArrayList<String> sitesExperience = new ArrayList<>();
                ArrayList<String> sitesExamples = new ArrayList<>();
                ArrayList<String> sitesUsers = new ArrayList<>();
                ArrayList<Integer> sitesApplicationIds = new ArrayList<>();

                ArrayList<String> appsMainNames = new ArrayList<>();
                ArrayList<String> appsAmbitions = new ArrayList<>();
                ArrayList<String> appsExperiens = new ArrayList<>();
                ArrayList<String> appsExamples = new ArrayList<>();
                ArrayList<String> appsUsers = new ArrayList<>();
                ArrayList<Integer> appsApplicationIds = new ArrayList<>();

                ArrayList<String> otherMainNames = new ArrayList<>();
                ArrayList<String> otherAmbitions = new ArrayList<>();
                ArrayList<String> otherExperience = new ArrayList<>();
                ArrayList<String> otherExamples = new ArrayList<>();
                ArrayList<String> otherUsers = new ArrayList<>();
                ArrayList<Integer> otherApplicationIds = new ArrayList<>();
                ArrayList<String> otherSection = new ArrayList();

                ArrayList<String> allMainNames = new ArrayList<>();
                ArrayList<String> allAmbitions = new ArrayList<>();
                ArrayList<String> allExperience = new ArrayList<>();
                ArrayList<String> allExamples = new ArrayList<>();
                ArrayList<String> allUsers = new ArrayList<>();
                ArrayList<Integer> allApplicationIds = new ArrayList<>();

                String bigName, name, section, bigSection;

                // сделаем treeMap, который по умолчанию отсортирован, но так как нужн обратный порядок
                // то передаем в конструкторе нужный порядок сортировки
                TreeMap<Integer, List<Integer>> mapCountToIds = new TreeMap<>(Collections.reverseOrder());

                DataSnapshot appTable = dataSnapshot.child("applications");
                int maxId = Integer.parseInt(appTable.child("maxId").getValue().toString());

                // подсчитываем количество найденых тегов для заявок
                for (int appIndex = 0; appIndex < maxId; appIndex++) {
                    // берем все теги заявки в исходном виде
                    DataSnapshot appNamesRaw = appTable.child("application" + appIndex).child("name");
                    DataSnapshot appPurposeRaw = appTable.child("application" + appIndex).child("purpose");
                    DataSnapshot appDescriptionRaw = appTable.child("application" + appIndex).child("descriptionApplication");
                    DataSnapshot appCanRaw = appTable.child("application" + appIndex).child("can");
                    if (appNamesRaw.getValue() != null && appPurposeRaw.getValue() != null && appDescriptionRaw != null
                            && appCanRaw != null) {
                        // разделяем их на отдельные слова по пробелу
                        List<String> appName = Arrays
                                .asList(appNamesRaw.getValue().toString().toLowerCase()
                                        .split(" ")
                                );
                        List<String> appPurpose = Arrays
                                .asList(appPurposeRaw.getValue().toString().toLowerCase()
                                        .split(" ")
                                );
                        List<String> appDescription = Arrays
                                .asList(appDescriptionRaw.getValue().toString().toLowerCase()
                                        .split(" ")
                                );
                        List<String> appCan = Arrays
                                .asList(appCanRaw.getValue().toString().toLowerCase()
                                        .split(" ")
                                );

                        // подсчитываем сколько из них подходит под поисковый запрос
                        int foundTagsCount = 0;
                        for (String word : searchWords) {
                            if (appName.contains(word)) foundTagsCount += 3;
                        }
                        for (String word : searchWords) {
                            if (appPurpose.contains(word)) foundTagsCount+=2;
                        }
                        for (String word : searchWords) {
                            if (appDescription.contains(word)) foundTagsCount++;
                        }
                        for (String word : searchWords) {
                            if (appCan.contains(word)) foundTagsCount++;
                        }

                        // сохраняем в мапу
                        // value как список, так как у разных заявок может быть одинаковое количество совпадений
                        if (foundTagsCount > 0) {
                            List<Integer> ids;
                            // смотрим, есть ли уже такое количество совпадений
                            // если есть, то добавляем в существующий список
                            if (mapCountToIds.containsKey(foundTagsCount))
                                ids = mapCountToIds.get(foundTagsCount);
                            else
                                ids = new ArrayList<>();

                            ids.add(appIndex);
                            mapCountToIds.put(foundTagsCount, ids);
                        }
                    }
                }
                // вот тут ты делал новую сущность (в mas.toArray()) и не использовал ее, то есть она сортировалась, а mas так и оставался не отсоритрованным
//                Arrays.sort(mas.toArray(), Collections.reverseOrder()); // сортируем массив по убыванию

                // до этого момента всё верно

                // так как мы взяли TreeMap, то там все уже отсортировано по убыванию
                // поэтому просто прогоняем цикл по всем ключам и значениям
                if (!mapCountToIds.isEmpty()) {
                    for (Map.Entry<Integer, List<Integer>> entry : mapCountToIds.entrySet()) {
                        for (int appId : entry.getValue()) {
                            DataSnapshot app = appTable.child("application" + appId);
                            DataSnapshot appSection = app.child("section");

                            // если почему-то нет такой записи или у нее нет section (какая-то ошибка), то пропускаем этот id
                            if (app.getValue() == null || appSection.getValue() == null) continue;

                            // чуть-чуть порядка :)
                            DataSnapshot appName = app.child("name");
                            DataSnapshot appPurpose = app.child("purpose");
                            DataSnapshot appExp = app.child("experience");
                            DataSnapshot appExample = app.child("example");
                            DataSnapshot appCreator = app.child("creator");

                            // если выше условие прошло, то это полюбому будет добавлено, поэтому вынес наверх
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                allMainNames.add("  " + bigName + "...");
                            } else {
                                allMainNames.add("  " + appName.getValue().toString());
                            }
                            if (appPurpose.getValue().toString().length() > 146) {
                                bigName = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigName += name.charAt(j);
                                }
                                allAmbitions.add(bigName + "...");
                            } else {
                                allAmbitions.add(appPurpose.getValue().toString());
                            }
                            allExperience.add("  Опыт: " + appExp.getValue().toString());
                            allExamples.add("  Пример работы: " + appExample.getValue().toString());
                            allUsers.add(appCreator.getValue().toString());
                            allApplicationIds.add(appId);

                            // обработка по секциям
                            if (appSection.getValue().toString().equals("Бизнес в интернете")) {
                                if (appName.getValue().toString().length() > 25) {
                                    bigName = "";
                                    name = appName.getValue().toString();
                                    for (int j = 0; j < 25; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    internetMainNames.add("  " + bigName + "...");
                                } else {
                                    internetMainNames.add("  " + appName.getValue().toString());
                                }
                                if (appPurpose.getValue().toString().length() > 146) {
                                    bigName = "";
                                    name = appPurpose.getValue().toString();
                                    for (int j = 0; j < 146; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    internetAmbitions.add(bigName + "...");
                                } else {
                                    internetAmbitions.add(appPurpose.getValue().toString());
                                }
                                internetExperience.add("  Опыт: " + appExp.getValue().toString());
                                internetExamples.add("  Пример работы: " + appExample.getValue().toString());
                                internetUsers.add(appCreator.getValue().toString());
                                internetApplicationIds.add(appId);
                            } else if (appSection.getValue() != null && appSection.getValue().equals("Оффлайн бизнес")) {
                                if (appName.getValue().toString().length() > 25) {
                                    bigName = "";
                                    name = appName.getValue().toString();
                                    for (int j = 0; j < 25; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    buisnessMainNames.add("  " + bigName + "...");
                                } else {
                                    buisnessMainNames.add("  " + appName.getValue().toString());
                                }
                                if (appPurpose.getValue().toString().length() > 146) {
                                    bigName = "";
                                    name = appPurpose.getValue().toString();
                                    for (int j = 0; j < 146; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    buisnessAmbitions.add(bigName + "...");
                                } else {
                                    buisnessAmbitions.add(appPurpose.getValue().toString());
                                }
                                buisnessExperience.add("  Опыт: " + appExp.getValue().toString());
                                buisnessExamples.add("  Пример работы: " + appExample.getValue().toString());
                                buisnessUsers.add(appCreator.getValue().toString());
                                buisnessApplicationIds.add(appId);
                            } else if (appSection.getValue() != null && appSection.getValue().equals("Создание игр")) {
                                if (appName.getValue().toString().length() > 25) {
                                    bigName = "";
                                    name = appName.getValue().toString();
                                    for (int j = 0; j < 25; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    gamesMainNames.add("  " + bigName + "...");
                                } else {
                                    gamesMainNames.add("  " + appName.getValue().toString());
                                }
                                if (appPurpose.getValue().toString().length() > 146) {
                                    bigName = "";
                                    name = appPurpose.getValue().toString();
                                    for (int j = 0; j < 146; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    gamesAbmitions.add(bigName + "...");
                                } else {
                                    gamesAbmitions.add(appPurpose.getValue().toString());
                                }
                                gamesExperience.add("  Опыт: " + appExp.getValue().toString());
                                gamesExamples.add("  Пример работы: " + appExample.getValue().toString());
                                gamesUsers.add(appCreator.getValue().toString());
                                gamesApplicationIds.add(appId);
                            } else if (appSection.getValue() != null && appSection.getValue().equals("Создание сайтов")) {
                                if (appName.getValue().toString().length() > 25) {
                                    bigName = "";
                                    name = appName.getValue().toString();
                                    for (int j = 0; j < 25; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    sitesMainNames.add("  " + bigName + "...");
                                } else {
                                    sitesMainNames.add("  " + appName.getValue().toString());
                                }
                                if (appPurpose.getValue().toString().length() > 146) {
                                    bigName = "";
                                    name = appPurpose.getValue().toString();
                                    for (int j = 0; j < 146; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    sitesAmbitions.add(bigName + "...");
                                } else {
                                    sitesAmbitions.add(appPurpose.getValue().toString());
                                }
                                sitesExperience.add("  Опыт: " + appExp.getValue().toString());
                                sitesExamples.add("  Пример работы: " + appExample.getValue().toString());
                                sitesUsers.add(appCreator.getValue().toString());
                                sitesApplicationIds.add(appId);
                            } else if (appSection.getValue() != null && appSection.getValue().equals("Создание приложений")) {
                                if (appName.getValue().toString().length() > 25) {
                                    bigName = "";
                                    name = appName.getValue().toString();
                                    for (int j = 0; j < 25; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    appsMainNames.add("  " + bigName + "...");
                                } else {
                                    appsMainNames.add("  " + appName.getValue().toString());
                                }
                                if (appPurpose.getValue().toString().length() > 146) {
                                    bigName = "";
                                    name = appPurpose.getValue().toString();
                                    for (int j = 0; j < 146; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    appsAmbitions.add(bigName + "...");
                                } else {
                                    appsAmbitions.add(appPurpose.getValue().toString());
                                }
                                appsExperiens.add("  Опыт: " + appExp.getValue().toString());
                                appsExamples.add("  Пример работы: " + appExample.getValue().toString());
                                appsUsers.add(appCreator.getValue().toString());
                                appsApplicationIds.add(appId);
                            } else {
                                if (appName.getValue().toString().length() > 25) {
                                    bigName = "";
                                    name = appName.getValue().toString();
                                    for (int j = 0; j < 25; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    otherMainNames.add("  " + bigName + "...");
                                } else {
                                    otherMainNames.add("  " + appName.getValue().toString());
                                }
                                if (appPurpose.getValue().toString().length() > 146) {
                                    bigName = "";
                                    name = appPurpose.getValue().toString();
                                    for (int j = 0; j < 146; j++) {
                                        bigName += name.charAt(j);
                                    }
                                    otherAmbitions.add(bigName + "...");
                                } else {
                                    otherAmbitions.add(appPurpose.getValue().toString());
                                }
                                otherExperience.add("  Опыт: " + appExp.getValue().toString());
                                otherExamples.add("  Пример работы: " + appExample.getValue().toString());
                                otherUsers.add(appCreator.getValue().toString());
                                if (appSection.getValue().toString().length() > 21) {
                                    bigSection = "";
                                    section = appSection.getValue().toString();
                                    for (int j = 0; j < 21; j++) {
                                        bigSection += section.charAt(j);
                                    }
                                    otherSection.add("  Раздел:  " + bigSection + "...");
                                } else {
                                    otherSection.add("  Раздел:  " + appSection.getValue().toString());
                                }
                                otherApplicationIds.add(appId);
                            }
                        }

                        arrAll[0] = new AdapterElement[allMainNames.size()];
                        arrInternet[0] = new AdapterElement[internetMainNames.size()];
                        arrBuisness[0] = new AdapterElement[buisnessMainNames.size()];
                        arrGames[0] = new AdapterElement[gamesMainNames.size()];
                        arrSites[0] = new AdapterElement[sitesMainNames.size()];
                        arrApps[0] = new AdapterElement[appsMainNames.size()];
                        arrOther[0] = new AdapterElementOther[otherMainNames.size()];
                    }
                    for (int i = 0; i < arrAll[0].length; i++) {
                        AdapterElement month = new AdapterElement();
                        month.mainName = allMainNames.get(i);
                        month.ambition = allAmbitions.get(i);
                        month.experience = allExperience.get(i);
                        month.example = allExamples.get(i);
                        month.user = allUsers.get(i);
                        month.applicationId = allApplicationIds.get(i).toString();
                        arrAll[0][i] = month;
                    }
                    for (int i = 0; i < arrBuisness[0].length; i++) {
                        AdapterElement month = new AdapterElement();
                        month.mainName = buisnessMainNames.get(i);
                        month.ambition = buisnessAmbitions.get(i);
                        month.experience = buisnessExperience.get(i);
                        month.example = buisnessExamples.get(i);
                        month.user = buisnessUsers.get(i);
                        month.applicationId = buisnessApplicationIds.get(i).toString();
                        arrBuisness[0][i] = month;
                    }
                    for (int i = 0; i < arrGames[0].length; i++) {
                        AdapterElement month = new AdapterElement();
                        month.mainName = gamesMainNames.get(i);
                        month.ambition = gamesAbmitions.get(i);
                        month.experience = gamesExperience.get(i);
                        month.example = gamesExamples.get(i);
                        month.user = gamesUsers.get(i);
                        month.applicationId = gamesApplicationIds.get(i).toString();
                        arrGames[0][i] = month;
                    }
                    for (int i = 0; i < arrSites[0].length; i++) {
                        AdapterElement month = new AdapterElement();
                        month.mainName = sitesMainNames.get(i);
                        month.ambition = sitesAmbitions.get(i);
                        month.experience = sitesExperience.get(i);
                        month.example = sitesExamples.get(i);
                        month.user = sitesUsers.get(i);
                        month.applicationId = sitesApplicationIds.get(i).toString();
                        arrSites[0][i] = month;
                    }
                    for (int i = 0; i < arrInternet[0].length; i++) {
                        AdapterElement month = new AdapterElement();
                        month.mainName = internetMainNames.get(i);
                        month.ambition = internetAmbitions.get(i);
                        month.experience = internetExperience.get(i);
                        month.example = internetExamples.get(i);
                        month.user = internetUsers.get(i);
                        month.applicationId = internetApplicationIds.get(i).toString();
                        arrInternet[0][i] = month;
                    }
                    for (int i = 0; i < arrApps[0].length; i++) {
                        AdapterElement month = new AdapterElement();
                        month.mainName = appsMainNames.get(i);
                        month.ambition = appsAmbitions.get(i);
                        month.experience = appsExperiens.get(i);
                        month.example = appsExamples.get(i);
                        month.user = appsUsers.get(i);
                        month.applicationId = appsApplicationIds.get(i).toString();
                        arrApps[0][i] = month;
                    }
                    for (int i = 0; i < arrOther[0].length; i++) {
                        AdapterElementOther month = new AdapterElementOther();
                        month.mainName = otherMainNames.get(i);
                        month.ambition = otherAmbitions.get(i);
                        month.experience = otherExperience.get(i);
                        month.example = otherExamples.get(i);
                        month.user = otherUsers.get(i);
                        month.applicationId = otherApplicationIds.get(i).toString();
                        month.sectionClass = otherSection.get(i);
                        arrOther[0][i] = month;
                    }

                    MainAdapter adapterAll = new MainAdapter(MostMainActivity.this, arrAll[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterAll.setUserActionListener(MostMainActivity.this);
                    lv.setAdapter(adapterAll);
                    MainAdapter adapterBuisness = new MainAdapter(MostMainActivity.this, arrBuisness[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterBuisness.setUserActionListener(MostMainActivity.this);
                    lv2.setAdapter(adapterBuisness);
                    MainAdapter adapterGames = new MainAdapter(MostMainActivity.this, arrGames[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterGames.setUserActionListener(MostMainActivity.this);
                    lv3.setAdapter(adapterGames);
                    MainAdapter adapterSites = new MainAdapter(MostMainActivity.this, arrSites[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterSites.setUserActionListener(MostMainActivity.this);
                    lv4.setAdapter(adapterSites);
                    MainAdapter adapterInternet = new MainAdapter(MostMainActivity.this, arrInternet[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterInternet.setUserActionListener(MostMainActivity.this);
                    lv5.setAdapter(adapterInternet);
                    MainAdapter adapterApps = new MainAdapter(MostMainActivity.this, arrApps[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterApps.setUserActionListener(MostMainActivity.this);
                    lv6.setAdapter(adapterApps);
                    MainAdapterForOther adapterOther = new MainAdapterForOther(MostMainActivity.this, arrOther[0], userName);
                    // выставляем слушателя в адаптер (слушатель – наше активити)
                    adapterOther.setUserActionListener(MostMainActivity.this);
                    lv7.setAdapter(adapterOther);
                    if (allMainNames.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Ничего не найдено", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Ничего не найдено", Toast.LENGTH_LONG).show();
                    lv.removeAllViewsInLayout();
                    lv2.removeAllViewsInLayout();
                    lv3.removeAllViewsInLayout();
                    lv4.removeAllViewsInLayout();
                    lv5.removeAllViewsInLayout();
                    lv6.removeAllViewsInLayout();
                    lv7.removeAllViewsInLayout();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Зашёл в onCancelled", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
    }

    private void fillData() {
        final AdapterElement[][] arrBuisness = new AdapterElement[1][];
        final AdapterElement[][] arrAll = new AdapterElement[1][];
        final AdapterElement[][] arrInternet = new AdapterElement[1][];
        final AdapterElement[][] arrGames = new AdapterElement[1][];
        final AdapterElement[][] arrSites = new AdapterElement[1][];
        final AdapterElement[][] arrApps = new AdapterElement[1][];
        final AdapterElementOther[][] arrOther = new AdapterElementOther[1][1];
        ValueEventListener listenerAtOnce = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "Зашёл в onDataChange", Toast.LENGTH_SHORT).show();

                ArrayList<String> internetMainNames = new ArrayList<>();
                ArrayList<String> internetAmbitions = new ArrayList<>();
                ArrayList<String> internetExperience = new ArrayList<>();
                ArrayList<String> internetExamples = new ArrayList<>();
                ArrayList<String> internetUsers = new ArrayList<>();
                ArrayList<Integer> internetApplicationIds = new ArrayList<>();

                ArrayList<String> buisnessMainNames = new ArrayList<>();
                ArrayList<String> buisnessAmbitions = new ArrayList<>();
                ArrayList<String> buisnessExperience = new ArrayList<>();
                ArrayList<String> buisnessExamples = new ArrayList<>();
                ArrayList<String> buisnessUsers = new ArrayList<>();
                ArrayList<Integer> buisnessApplicationIds = new ArrayList<>();

                ArrayList<String> gamesMainNames = new ArrayList<>();
                ArrayList<String> gamesAbmitions = new ArrayList<>();
                ArrayList<String> gamesExperience = new ArrayList<>();
                ArrayList<String> gamesExamples = new ArrayList<>();
                ArrayList<String> gamesUsers = new ArrayList<>();
                ArrayList<Integer> gamesApplicationIds = new ArrayList<>();

                ArrayList<String> sitesMainNames = new ArrayList<>();
                ArrayList<String> sitesAmbitions = new ArrayList<>();
                ArrayList<String> sitesExperience = new ArrayList<>();
                ArrayList<String> sitesExamples = new ArrayList<>();
                ArrayList<String> sitesUsers = new ArrayList<>();
                ArrayList<Integer> sitesApplicationIds = new ArrayList<>();

                ArrayList<String> appsMainNames = new ArrayList<>();
                ArrayList<String> appsAmbitions = new ArrayList<>();
                ArrayList<String> appsExperiens = new ArrayList<>();
                ArrayList<String> appsExamples = new ArrayList<>();
                ArrayList<String> appsUsers = new ArrayList<>();
                ArrayList<Integer> appsApplicationIds = new ArrayList<>();

                ArrayList<String> otherMainNames = new ArrayList<>();
                ArrayList<String> otherAmbitions = new ArrayList<>();
                ArrayList<String> otherExperience = new ArrayList<>();
                ArrayList<String> otherExamples = new ArrayList<>();
                ArrayList<String> otherUsers = new ArrayList<>();
                ArrayList<Integer> otherApplicationIds = new ArrayList<>();
                ArrayList<String> otherSection = new ArrayList();

                ArrayList<String> allMainNames = new ArrayList<>();
                ArrayList<String> allAmbitions = new ArrayList<>();
                ArrayList<String> allExperience = new ArrayList<>();
                ArrayList<String> allExamples = new ArrayList<>();
                ArrayList<String> allUsers = new ArrayList<>();
                ArrayList<Integer> allApplicationIds = new ArrayList<>();

                String bigName, name, section, bigSection;

                DataSnapshot appTable = dataSnapshot.child("applications");
                int maxId = Integer.parseInt(appTable.child("maxId").getValue().toString());

                for (int appId = 0; appId < maxId; appId++) {

                    DataSnapshot app = appTable.child("application" + appId);
                    DataSnapshot appName = app.child("name");
                    DataSnapshot appPurpose = app.child("purpose");
                    DataSnapshot appExp = app.child("experience");
                    DataSnapshot appExample = app.child("example");
                    DataSnapshot appCreator = app.child("creator");
                    DataSnapshot appSection = app.child("section");

                    if (appName.getValue() != null) {

                        if (appName.getValue().toString().length() > 25) {
                            bigName = "";
                            name = appName.getValue().toString();
                            for (int j = 0; j < 25; j++) {
                                bigName += name.charAt(j);
                            }
                            allMainNames.add("  " + bigName + "...");
                        } else {
                            allMainNames.add("  " + appName.getValue().toString());
                        }
                        if (appPurpose.getValue().toString().length() > 146) {
                            bigName = "";
                            name = appPurpose.getValue().toString();
                            for (int j = 0; j < 146; j++) {
                                bigName += name.charAt(j);
                            }
                            allAmbitions.add(bigName + "...");
                        } else {
                            allAmbitions.add(appPurpose.getValue().toString());
                        }
                        allExperience.add("  Опыт: " + appExp.getValue().toString());
                        allExamples.add("  Пример работы: " + appExample.getValue().toString());
                        allUsers.add(appCreator.getValue().toString());
                        allApplicationIds.add(appId);

                        // обработка по секциям
                        if (appSection.getValue().toString().equals("Бизнес в интернете")) {
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                internetMainNames.add("  " + bigName + "...");
                            } else {
                                internetMainNames.add("  " + appName.getValue().toString());
                            }
                            if (appPurpose.getValue().toString().length() > 146) {
                                bigName = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigName += name.charAt(j);
                                }
                                internetAmbitions.add(bigName + "...");
                            } else {
                                internetAmbitions.add(appPurpose.getValue().toString());
                            }
                            internetExperience.add("  Опыт: " + appExp.getValue().toString());
                            internetExamples.add("  Пример работы: " + appExample.getValue().toString());
                            internetUsers.add(appCreator.getValue().toString());
                            internetApplicationIds.add(appId);
                        } else if (appSection.getValue() != null && appSection.getValue().equals("Оффлайн бизнес")) {
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                buisnessMainNames.add("  " + bigName + "...");
                            } else {
                                buisnessMainNames.add("  " + appName.getValue().toString());
                            }
                            if (appPurpose.getValue().toString().length() > 146) {
                                bigName = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigName += name.charAt(j);
                                }
                                buisnessAmbitions.add(bigName + "...");
                            } else {
                                buisnessAmbitions.add(appPurpose.getValue().toString());
                            }
                            buisnessExperience.add("  Опыт: " + appExp.getValue().toString());
                            buisnessExamples.add("  Пример работы: " + appExample.getValue().toString());
                            buisnessUsers.add(appCreator.getValue().toString());
                            buisnessApplicationIds.add(appId);
                        } else if (appSection.getValue() != null && appSection.getValue().equals("Создание игр")) {
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                gamesMainNames.add("  " + bigName + "...");
                            } else {
                                gamesMainNames.add("  " + appName.getValue().toString());
                            }
                            if (appPurpose.getValue().toString().length() > 146) {
                                bigName = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigName += name.charAt(j);
                                }
                                gamesAbmitions.add(bigName + "...");
                            } else {
                                gamesAbmitions.add(appPurpose.getValue().toString());
                            }
                            gamesExperience.add("  Опыт: " + appExp.getValue().toString());
                            gamesExamples.add("  Пример работы: " + appExample.getValue().toString());
                            gamesUsers.add(appCreator.getValue().toString());
                            gamesApplicationIds.add(appId);
                        } else if (appSection.getValue() != null && appSection.getValue().equals("Создание сайтов")) {
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                sitesMainNames.add("  " + bigName + "...");
                            } else {
                                sitesMainNames.add("  " + appName.getValue().toString());
                            }
                            if (appPurpose.getValue().toString().length() > 146) {
                                bigName = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigName += name.charAt(j);
                                }
                                sitesAmbitions.add(bigName + "...");
                            } else {
                               sitesAmbitions.add(appPurpose.getValue().toString());
                            }
                            sitesExperience.add("  Опыт: " + appExp.getValue().toString());
                            sitesExamples.add("  Пример работы: " + appExample.getValue().toString());
                            sitesUsers.add(appCreator.getValue().toString());
                            sitesApplicationIds.add(appId);
                        } else if (appSection.getValue() != null && appSection.getValue().equals("Создание приложений")) {
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                appsMainNames.add("  " + bigName + "...");
                            } else {
                                appsMainNames.add("  " + appName.getValue().toString());
                            }
                            if (appPurpose.getValue().toString().length() > 146) {
                                bigName = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigName += name.charAt(j);
                                }
                                appsAmbitions.add(bigName + "...");
                            } else {
                                appsAmbitions.add(appPurpose.getValue().toString());
                            }
                            appsExperiens.add("  Опыт: " + appExp.getValue().toString());
                            appsExamples.add("  Пример работы: " + appExample.getValue().toString());
                            appsUsers.add(appCreator.getValue().toString());
                            appsApplicationIds.add(appId);
                        } else {
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                otherMainNames.add("  " + bigName + "...");
                            } else {
                                otherMainNames.add("  " + appName.getValue().toString());
                            }
                            if (appPurpose.getValue().toString().length() > 146) {
                                bigName = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigName += name.charAt(j);
                                }
                                otherAmbitions.add(bigName + "...");
                            } else {
                                otherAmbitions.add(appPurpose.getValue().toString());
                            }
                            otherExperience.add("  Опыт: " + appExp.getValue().toString());
                            otherExamples.add("  Пример работы: " + appExample.getValue().toString());
                            otherUsers.add(appCreator.getValue().toString());
                            if (appSection.getValue().toString().length() > 21) {
                                bigSection = "";
                                section = appSection.getValue().toString();
                                for (int j = 0; j < 21; j++) {
                                    bigSection += section.charAt(j);
                                }
                                otherSection.add("  Раздел:  " + bigSection + "...");
                            } else {
                                otherSection.add("  Раздел:  " + appSection.getValue().toString());
                            }
                            otherApplicationIds.add(appId);
                        }
                    }
                }
                arrAll[0] = new AdapterElement[allMainNames.size()];
                arrInternet[0] = new AdapterElement[internetMainNames.size()];
                arrBuisness[0] = new AdapterElement[buisnessMainNames.size()];
                arrGames[0] = new AdapterElement[gamesMainNames.size()];
                arrSites[0] = new AdapterElement[sitesMainNames.size()];
                arrApps[0] = new AdapterElement[appsMainNames.size()];
                arrOther[0] = new AdapterElementOther[otherMainNames.size()];

                for (int i = 0; i < arrAll[0].length; i++) {
                    AdapterElement month = new AdapterElement();
                    month.mainName = allMainNames.get(i);
                    month.ambition = allAmbitions.get(i);
                    month.experience = allExperience.get(i);
                    month.example = allExamples.get(i);
                    month.user = allUsers.get(i);
                    month.applicationId = allApplicationIds.get(i).toString();
                    arrAll[0][i] = month;
                }
                for (int i = 0; i < arrBuisness[0].length; i++) {
                    AdapterElement month = new AdapterElement();
                    month.mainName = buisnessMainNames.get(i);
                    month.ambition = buisnessAmbitions.get(i);
                    month.experience = buisnessExperience.get(i);
                    month.example = buisnessExamples.get(i);
                    month.user = buisnessUsers.get(i);
                    month.applicationId = buisnessApplicationIds.get(i).toString();
                    arrBuisness[0][i] = month;
                }
                for (int i = 0; i < arrGames[0].length; i++) {
                    AdapterElement month = new AdapterElement();
                    month.mainName = gamesMainNames.get(i);
                    month.ambition = gamesAbmitions.get(i);
                    month.experience = gamesExperience.get(i);
                    month.example = gamesExamples.get(i);
                    month.user = gamesUsers.get(i);
                    month.applicationId = gamesApplicationIds.get(i).toString();
                    arrGames[0][i] = month;
                }
                for (int i = 0; i < arrSites[0].length; i++) {
                    AdapterElement month = new AdapterElement();
                    month.mainName = sitesMainNames.get(i);
                    month.ambition = sitesAmbitions.get(i);
                    month.experience = sitesExperience.get(i);
                    month.example = sitesExamples.get(i);
                    month.user = sitesUsers.get(i);
                    month.applicationId = sitesApplicationIds.get(i).toString();
                    arrSites[0][i] = month;
                }
                for (int i = 0; i < arrInternet[0].length; i++) {
                    AdapterElement month = new AdapterElement();
                    month.mainName = internetMainNames.get(i);
                    month.ambition = internetAmbitions.get(i);
                    month.experience = internetExperience.get(i);
                    month.example = internetExamples.get(i);
                    month.user = internetUsers.get(i);
                    month.applicationId = internetApplicationIds.get(i).toString();
                    arrInternet[0][i] = month;
                }
                for (int i = 0; i < arrApps[0].length; i++) {
                    AdapterElement month = new AdapterElement();
                    month.mainName = appsMainNames.get(i);
                    month.ambition = appsAmbitions.get(i);
                    month.experience = appsExperiens.get(i);
                    month.example = appsExamples.get(i);
                    month.user = appsUsers.get(i);
                    month.applicationId = appsApplicationIds.get(i).toString();
                    arrApps[0][i] = month;
                }
                for (int i = 0; i < arrOther[0].length; i++) {
                    AdapterElementOther month = new AdapterElementOther();
                    month.mainName = otherMainNames.get(i);
                    month.ambition = otherAmbitions.get(i);
                    month.experience = otherExperience.get(i);
                    month.example = otherExamples.get(i);
                    month.user = otherUsers.get(i);
                    month.applicationId = otherApplicationIds.get(i).toString();
                    month.sectionClass = otherSection.get(i);
                    arrOther[0][i] = month;
                }

                MainAdapter adapterAll = new MainAdapter(MostMainActivity.this, arrAll[0], userName);
                // выставляем слушателя в адаптер (слушатель – наше активити)
                adapterAll.setUserActionListener(MostMainActivity.this);
                lv.setAdapter(adapterAll);
                MainAdapter adapterBuisness = new MainAdapter(MostMainActivity.this, arrBuisness[0], userName);
                // выставляем слушателя в адаптер (слушатель – наше активити)
                adapterBuisness.setUserActionListener(MostMainActivity.this);
                lv2.setAdapter(adapterBuisness);
                MainAdapter adapterGames = new MainAdapter(MostMainActivity.this, arrGames[0], userName);
                // выставляем слушателя в адаптер (слушатель – наше активити)
                adapterGames.setUserActionListener(MostMainActivity.this);
                lv3.setAdapter(adapterGames);
                MainAdapter adapterSites = new MainAdapter(MostMainActivity.this, arrSites[0], userName);
                // выставляем слушателя в адаптер (слушатель – наше активити)
                adapterSites.setUserActionListener(MostMainActivity.this);
                lv4.setAdapter(adapterSites);
                MainAdapter adapterInternet = new MainAdapter(MostMainActivity.this, arrInternet[0], userName);
                // выставляем слушателя в адаптер (слушатель – наше активити)
                adapterInternet.setUserActionListener(MostMainActivity.this);
                lv5.setAdapter(adapterInternet);
                MainAdapter adapterApps = new MainAdapter(MostMainActivity.this, arrApps[0], userName);
                // выставляем слушателя в адаптер (слушатель – наше активити)
                adapterApps.setUserActionListener(MostMainActivity.this);
                lv6.setAdapter(adapterApps);
                MainAdapterForOther adapterOther = new MainAdapterForOther(MostMainActivity.this, arrOther[0], userName);
                // выставляем слушателя в адаптер (слушатель – наше активити)
                adapterOther.setUserActionListener(MostMainActivity.this);
                lv7.setAdapter(adapterOther);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Зашёл в onCancelled", Toast.LENGTH_SHORT).show();
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.favoriets) {
            Intent intent = new Intent(MostMainActivity.this, Chosen.class);
            intent.putExtra(Chosen.PARAM_USER_NAME, userName);
            startActivity(intent);
        } else if (id == R.id.my_applications) {
            Intent intentApplications = new Intent(MostMainActivity.this, MyApplications.class);
            intentApplications.putExtra(MyApplications.PARAM_USER_NAME, userName);
            startActivity(intentApplications);
        } else if (id == R.id.changing_describtion) {
            ValueEventListener listenerAtOnceDescription = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int clientId = -1;
                    for (int i = 0; i < Integer.valueOf(dataSnapshot.child("maxId").getValue().toString()); i++) {
                        // == userName нельзя проверять. String проверять только через equals
                        if (userName.equals(dataSnapshot.child("client" + i).child("login").getValue())) {
                            clientId = i;
                            break;
                        }
                    }

                    final DatabaseReference description = mDatabase.child("client" + clientId).child("description");

                    // а из-за строчек ниже приложение вылетает при нажатии на кнопку "Отредактировать"
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MostMainActivity.this);
                    builder2.setTitle("Редактирование описания")
                            .setCancelable(false)
                            .setNegativeButton("Отредактировать",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            description.setValue(edit.getText().toString());
                                            dialog.cancel();
                                        }
                                    });
                    LayoutInflater ltInflater = getLayoutInflater();
                    View view = ltInflater.inflate(R.layout.dialog_signin, null, false);
                    // только так ты можешь получить нужный тебе editText, потому что он лежит только в этом лейауте
                    // раньше ты пытался найти его на активити MostMainActivity
                    edit = view.findViewById(R.id.editText6);
                    edit.setText(dataSnapshot.child("client" + clientId).child("description").getValue().toString());
                    AlertDialog alert2 = builder2.create();
                    alert2.setView(view);
                    alert2.getWindow().setLayout(265, 130);
                    alert2.show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.addListenerForSingleValueEvent(listenerAtOnceDescription);
        } else if (id == R.id.sing_out) {
            Intent intentSignOut = new Intent(MostMainActivity.this, LoginActivity.class);
            startActivity(intentSignOut);

            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void setupTabs() {
        setTitle("TabHost");
        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");

        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Все заявки");
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
        tabSpec.setIndicator("Бизнес в интернете");
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
    }


    @Override
    public void onShowMoreClick(final String applicationId) {
        // нажали на кнопку, а действие сюда прилетело
        ValueEventListener listenerAtOnce = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent intent = new Intent(MostMainActivity.this, moreAboutApplication.class);
                intent.putExtra("applId", applicationId);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MostMainActivity.this, "Зашёл в onCancelled", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
    }
}