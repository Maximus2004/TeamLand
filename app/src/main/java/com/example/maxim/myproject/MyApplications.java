package com.example.maxim.myproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyApplications extends AppCompatActivity implements MainAdapterForMyAppl.UserActionListener {
    public static String TAG = "MyApplications";
    public static String PARAM_USER_NAME = TAG + ".userName";
    DatabaseReference mDatabase;
    String userId;
    RecyclerView rv;

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
        rv.setLayoutManager(new LinearLayoutManager(this));
        fillData();
    }

    void fillData() {
        ValueEventListener listenerAtOnce = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot appTable = dataSnapshot.child("applications");
                ArrayList<AppModel> apps = new ArrayList<AppModel>();
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.child("applications").getChildren();
                String user = dataSnapshot.child("users").child(userId).child("login").getValue().toString();

                for (DataSnapshot aSnapshotIterable : snapshotIterable) {
                    if (aSnapshotIterable.getKey().toString().equals("maxId")) break;
                    String appId = aSnapshotIterable.getKey().toString();
                    String creator = appTable.child(appId).child("creator").getValue().toString();

                    if (creator.equals(user)) {
                        DataSnapshot app = dataSnapshot.child("applications").child(aSnapshotIterable.getKey().toString());
                        AppModel appModel = app.getValue(AppModel.class);

                        apps.add(appModel);
                    }
                }

                MainAdapterForMyAppl adapter = new MainAdapterForMyAppl(apps, userId);
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
                mDatabase.child("applications").child(applicationId).removeValue();
                fillData();
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
