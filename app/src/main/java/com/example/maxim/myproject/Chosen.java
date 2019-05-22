package com.example.maxim.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chosen extends AppCompatActivity implements MainAdapter.UserActionListener {
    public static String TAG = "Chosen";
    public static String PARAM_USER_NAME = TAG + ".userName";
    DatabaseReference mDatabase;
    ListView lv;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen);
        Intent intent = getIntent();
        userName = intent.getStringExtra(PARAM_USER_NAME);

        Toolbar toolbar2 = findViewById(R.id.toolbarChosen);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Избранные");

        //Intent intent = getIntent();
        //userName = intent.getStringExtra(CreateApplication.PARAM_USER_NAME);

        lv = findViewById(R.id.listViewFavourite);
        makeMonth();
    }

    private void makeMonth() {
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
                        experiences.add("  Опыт: " + dataSnapshot.child("applications").child("application" + i + "").child("experience").getValue().toString());
                        examples.add("  Пример работы: " + dataSnapshot.child("applications").child("application" + i + "").child("example").getValue().toString());
                        users.add(dataSnapshot.child("applications").child("application" + i + "").child("creator").getValue().toString());
                        applicationIdes.add(i + "");
                    }
                }
                arr[0] = new AdapterElement[mainNames.size()];
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


                MainAdapter adapter = new MainAdapter(Chosen.this, arr[0], userName);
                // выставляем слушателя в адаптер (слушатель – наше активити)
                adapter.setUserActionListener(Chosen.this);
                lv.setAdapter(adapter);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onShowMoreClick(final String applicationId) {
        // нажали на кнопку, а действие сюда прилетело
        ValueEventListener listenerAtOnce = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent intent = new Intent(Chosen.this, moreAboutApplication.class);
                intent.putExtra("applId", applicationId);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Chosen.this, "Зашёл в onCancelled", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
    }
}