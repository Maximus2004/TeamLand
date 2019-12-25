package com.example.maxim.myproject.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import com.example.maxim.myproject.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maxim.myproject.db.FilterAppsResultListener;
import com.example.maxim.myproject.model.AppModel;
import com.example.maxim.myproject.model.AppSection;
import com.example.maxim.myproject.ui.fragment.AppsAdapter;
import com.example.maxim.myproject.db.FilterAppsResultListener;
import com.example.maxim.myproject.ui.fragment.AppsListFragment;
import com.example.maxim.myproject.db.util.CorrectDbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LeastMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "MostMainActivity";
    public static final String PARAM_USER_ID = TAG + ".username";
    public static final String PARAM_SORT = "sort";
    String userId;
    EditText edit;
    public static boolean[] chosen = new boolean[3];
    public static Boolean isSort = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        Intent intentUser = getIntent();
        userId = intentUser.getStringExtra(PARAM_USER_ID);
        Log.i("USERID", userId);
        Intent intentSort = getIntent();
        chosen = intentSort.getBooleanArrayExtra(PARAM_SORT);
        init();
    }

    private void init() {
        // настройка навигации
        setupNavigation();

        // загружаем "таблицу" заявок
        CorrectDbHelper.getInstance().loadApps();

        // настраиваем ViewPager
        ViewPager viewPager = findViewById(R.id.main_view_pager);
        // реализуем работу с фрагментами во ViewPager
        FragmentStatePagerAdapter fragmentsAdapter = getFragmentsAdapter();
        viewPager.setAdapter(fragmentsAdapter);

        // настраиваем табы для ViewPager, переключение которых будет менять фрагменты
        TabLayout tabLayout = findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // настройка кнопки создания заяки
        setupCreateAppButton();

        // измнение содержания nav_header
        //renameNavHeader();

        // настройка сортировки
        setupSort();
    }

    private void setupSort() {
        View.OnClickListener onSort = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeastMainActivity.this, SortActivity.class);
                startActivity(intent);
                //finish();
                //AppsListFragment.newInstance(AppSection.ALL, userId).loadDbData();
                //CorrectDbHelper.getInstance().loadApps();
            }
        };

        Button sort = findViewById(R.id.buttonSort);
        sort.setOnClickListener(onSort);
    }

    /*private void renameNavHeader() {
        ValueEventListener listenerAtOnce = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView headrenameNavHeaderer = (TextView) findViewById(R.id.headerTextView);
                header.setText(Objects.requireNonNull(dataSnapshot.child("users").child(userId).child("login").getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Возникла ошибка", Toast.LENGTH_SHORT);
                toast.show();
            }
        };
        CorrectDbHelper.getDatabase().addListenerForSingleValueEvent(listenerAtOnce);
    }*/

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

        ImageButton burger = findViewById(R.id.imageButton);
        View.OnClickListener oclBurger = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        };
        // присвоим обработчик кнопке burger
        burger.setOnClickListener(oclBurger);
    }

    private void setupCreateAppButton() {
        View.OnClickListener onCreateAppClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeastMainActivity.this, CreateApplication.class);
                intent.putExtra(CreateApplication.PARAM_USER_NAME, userId);
                startActivity(intent);
            }
        };

        ImageButton createAppButton = findViewById(R.id.imageBtn);
        createAppButton.setOnClickListener(onCreateAppClickListener);
    }

    /**
     * Создаем адаптер фрагментов для ViewPager
     *
     * @return FragmentStatePagerAdapter
     */
    private FragmentStatePagerAdapter getFragmentsAdapter() {
        // создаем фрагменты, в которых будут списки по секциям
        // список состоит из пар: название вкладки и фрагмент
        // странно, что final, но тем не менее мы можем добавлять пары
        final List<Pair<String, AppsListFragment>> fragments = new ArrayList<>();
        fragments.add(Pair.create(AppSection.ALL.getName(), AppsListFragment.newInstance(AppSection.ALL, userId)));
        fragments.add(Pair.create(AppSection.APPS.getName(), AppsListFragment.newInstance(AppSection.APPS, userId)));
        fragments.add(Pair.create(AppSection.BUSINESS.getName(), AppsListFragment.newInstance(AppSection.BUSINESS, userId)));
        fragments.add(Pair.create(AppSection.INTERNET.getName(), AppsListFragment.newInstance(AppSection.INTERNET, userId)));
        fragments.add(Pair.create(AppSection.SITES.getName(), AppsListFragment.newInstance(AppSection.SITES, userId)));
        fragments.add(Pair.create(AppSection.GAMES.getName(), AppsListFragment.newInstance(AppSection.GAMES, userId)));
        fragments.add(Pair.create(AppSection.OTHER.getName(), AppsListFragment.newInstance(AppSection.OTHER, userId)));

        // создаем адаптер на основе списка
        return new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position).second;
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return fragments.get(position).first;
            }
        };
    }

    @Override
    protected void onStop() {
        // очищаем подписку на обновления "таблицы" заявок
        CorrectDbHelper.getInstance().clearAppsListener();
        super.onStop();
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
            Intent intent = new Intent(LeastMainActivity.this, Chosen.class);
            intent.putExtra(Chosen.PARAM_USER_NAME, userId);
            startActivity(intent);
        } else if (id == R.id.my_applications) {
            Intent intentApplications = new Intent(LeastMainActivity.this, MyApplications.class);
            intentApplications.putExtra(MyApplications.PARAM_USER_NAME, userId);
            startActivity(intentApplications);
        } else if (id == R.id.changing_describtion) {
            final DatabaseReference description = CorrectDbHelper.getInstance().getDescription(userId);
            AlertDialog.Builder builder = new AlertDialog.Builder(LeastMainActivity.this);
            builder.setTitle("Редактирование описания")
                    .setCancelable(false)
                    .setPositiveButton("Отредактировать", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            description.setValue(edit.getText().toString());
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            LayoutInflater ltInflater = getLayoutInflater();
            View view = ltInflater.inflate(R.layout.dialog_changing_description, null, false);
            edit = view.findViewById(R.id.editText6);
            edit.setText(CorrectDbHelper.dataSnapshot.child("users").child(userId).child("description").getValue().toString());
            AlertDialog alert = builder.create();
            alert.setView(view);
            alert.getWindow().setLayout(265, 130);
            alert.show();


        } else if (id == R.id.sing_out) {
            Intent intentSignOut = new Intent(LeastMainActivity.this, LoginActivity.class);
            startActivity(intentSignOut);

            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void changingActivities(final String applicationId) {
        Intent intent = new Intent(LeastMainActivity.this, MoreAboutApplicationActivity.class);
        intent.putExtra("applId", applicationId);
        startActivity(intent);
    }

   /* @Override
    public void onShowMoreClick(final String applicationId) {
        // нажали на кнопку, а действие сюда прилетело
        Intent intent = new Intent(LeastMainActivity.this, MoreAboutApplicationActivity.class);
        intent.putExtra("applId", applicationId);
        startActivity(intent);
    }*/
}
