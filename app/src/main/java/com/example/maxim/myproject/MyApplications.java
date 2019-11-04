package com.example.maxim.myproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyApplications extends AppCompatActivity implements MainAdapter.UserActionListener {
    public static String TAG = "MyApplications";
    public static String PARAM_USER_NAME = TAG + ".userName";
    DatabaseReference mDatabase;
    String userId;
    RecyclerView rv;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_applications);
        Intent intent = getIntent();
        userId = intent.getStringExtra(PARAM_USER_NAME);

        Toolbar toolbar2 = findViewById(R.id.toolbarAppl);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Мои заявки");

        rv = findViewById(R.id.recycler_view_myappl);
        makeMonth();
    }

    private void makeMonth() {
        ValueEventListener listenerAtOnce = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showToast("onDataChange MyAppl");
                DataSnapshot appTable = dataSnapshot.child("applications");
                ArrayList<AdapterElement> apps = new ArrayList<AdapterElement>();
                String bigName, name, bigNameAmb;

                for (int i = 0; i < Integer.parseInt(dataSnapshot.child("applications").child("maxId").getValue().toString()); i++) {
                    if (dataSnapshot.child("applications").child("application" + i).getValue() != null &&
                            dataSnapshot.child("applications").child("application" + i).child("creator").getValue().toString()
                                    .equals(dataSnapshot.child("users").child(userId).child("login").getValue().toString())) {
                        DataSnapshot app = appTable.child("application" + i);
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
                                bigName += "...";
                            } else
                                bigName = appName.getValue().toString();

                            if (appPurpose.getValue().toString().length() > 146) {
                                bigNameAmb = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigNameAmb += name.charAt(j);
                                }
                                bigNameAmb += "...";
                            } else
                                bigNameAmb = appPurpose.getValue().toString();

                            showToast(bigName);
                            apps.add(new AdapterElement("  " + bigName, appCreator.getValue().toString(), String.valueOf(i),
                                    appExp.getValue().toString(), appExample.getValue().toString(), bigNameAmb));
                        }
                    }
                }

                MainAdapter adapter = new MainAdapter(apps, userId);
                // выставляем слушателя в адаптер (слушатель – наше активити)
                adapter.setUserActionListener(MyApplications.this);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Возникла ошибка", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //MostMainActivity.fillData();
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
                mDatabase.child("applications").child("application" + applicationId + "").removeValue();
                makeMonth();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyApplications.this, "Ошибка!", Toast.LENGTH_SHORT).show();
            }
        };
        mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
