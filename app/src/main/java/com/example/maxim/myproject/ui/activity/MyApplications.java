package com.example.maxim.myproject.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.maxim.myproject.R;

import android.view.MenuItem;
import android.widget.Toast;

import com.example.maxim.myproject.AppModel;
import com.example.maxim.myproject.MainAdapterForMyAppl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import static com.example.maxim.myproject.db.util.CorrectDbHelper.dataSnapshot;

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
        mDatabase.child("applications").child(applicationId).removeValue();
        fillData();
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
