package com.example.maxim.myproject;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelper {
    static DatabaseReference mDatabase;
    static ArrayList<AppModel> appsAll = new ArrayList<AppModel>();
    static ArrayList<AppModel> appsInternet = new ArrayList<AppModel>();
    static ArrayList<AppModel> appsBuisness = new ArrayList<AppModel>();
    static ArrayList<AppModel> apps = new ArrayList<AppModel>();
    static ArrayList<AppModel> appsSites = new ArrayList<AppModel>();
    static ArrayList<AppModel> appsGames = new ArrayList<AppModel>();
    static ArrayList<AppModel> appsOthers = new ArrayList<AppModel>();

    public static void fillDataAllOther() {
        ValueEventListener listenerAtOnce = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                apps.clear();
                appsAll.clear();
                appsBuisness.clear();
                appsGames.clear();
                appsInternet.clear();
                appsOthers.clear();
                appsSites.clear();
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.child("applications").getChildren();
                //Collections.reverse((List<?>) snapshotIterable);

                for (DataSnapshot aSnapshotIterable : snapshotIterable) {
                    //Log.i("APPL", aSnapshotIterable.getKey().toString());
                    if (aSnapshotIterable.getKey().toString().equals("maxId")) break;
                    DataSnapshot app = dataSnapshot.child("applications").child(aSnapshotIterable.getKey().toString());
                    AppModel appModel = app.getValue(AppModel.class);

                    appsAll.add(appModel);

                    // обработка по секциям
                    if (appModel.section.equals("Бизнес в интернете")) {
                        appsInternet.add(appModel);
                    } else if (appModel.section.equals("Оффлайн бизнес")) {
                        appsBuisness.add(appModel);
                    } else if (appModel.section.equals("Создание игр")) {
                        appsGames.add(appModel);
                    } else if (appModel.section.equals("Создание сайтов")) {
                        appsSites.add(appModel);
                    } else if (appModel.section.equals("Создание приложений")) {
                        apps.add(appModel);
                    } else {
                        appsOthers.add(appModel);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
    }
}
