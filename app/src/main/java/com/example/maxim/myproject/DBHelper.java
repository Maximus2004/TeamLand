package com.example.maxim.myproject;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DBHelper {
    static DatabaseReference mDatabase;
    static ArrayList<AdapterElement> appsAll = new ArrayList<AdapterElement>();
    static ArrayList<AdapterElement> appsInternet = new ArrayList<AdapterElement>();
    static ArrayList<AdapterElement> appsBuisness = new ArrayList<AdapterElement>();
    static ArrayList<AdapterElement> apps = new ArrayList<AdapterElement>();
    static ArrayList<AdapterElement> appsSites = new ArrayList<AdapterElement>();
    static ArrayList<AdapterElement> appsGames = new ArrayList<AdapterElement>();
    static ArrayList<AdapterElement> appsOthers = new ArrayList<AdapterElement>();

    public static void fillDataAllOther() {
        ValueEventListener listenerAtOnce = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String bigName, name, section, bigSection, bigNameAmb;

                DataSnapshot appTable = dataSnapshot.child("applications");
                int maxId = Integer.parseInt(appTable.child("maxId").getValue().toString());

                apps.clear();
                appsAll.clear();
                appsBuisness.clear();
                appsGames.clear();
                appsInternet.clear();
                appsOthers.clear();
                appsSites.clear();

                for (int appId = maxId; appId > -1; appId--) {
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
                            bigName += "...";
                        }
                        else
                            bigName = appName.getValue().toString();

                        if (appPurpose.getValue().toString().length() > 146) {
                            bigNameAmb = "";
                            name = appPurpose.getValue().toString();
                            for (int j = 0; j < 146; j++) {
                                bigNameAmb += name.charAt(j);
                            }
                            bigNameAmb += "...";
                        }
                        else
                            bigNameAmb = appPurpose.getValue().toString();

                        appsAll.add(new AdapterElement("  " + bigName, appCreator.getValue().toString(), String.valueOf(appId),
                                appExp.getValue().toString(), appExample.getValue().toString(), bigNameAmb));

                        // обработка по секциям
                        if (appSection.getValue().toString().equals("Бизнес в интернете")) {
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                bigName += "...";
                            }
                            else
                                bigName = appName.getValue().toString();

                            if (appPurpose.getValue().toString().length() > 146) {
                                bigNameAmb = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigNameAmb += name.charAt(j);
                                }
                                bigNameAmb += "...";
                            }
                            else
                                bigNameAmb = appPurpose.getValue().toString();

                            appsInternet.add(new AdapterElement("  " + bigName, appCreator.getValue().toString(), String.valueOf(appId),
                                    appExp.getValue().toString(), appExample.getValue().toString(), bigNameAmb));
                        } else if (appSection.getValue() != null && appSection.getValue().equals("Оффлайн бизнес")) {
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                bigName += "...";
                            }
                            else
                                bigName = appName.getValue().toString();

                            if (appPurpose.getValue().toString().length() > 146) {
                                bigNameAmb = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigNameAmb += name.charAt(j);
                                }
                                bigNameAmb += "...";
                            }
                            else
                                bigNameAmb = appPurpose.getValue().toString();

                            appsBuisness.add(new AdapterElement("  " + bigName, appCreator.getValue().toString(), String.valueOf(appId),
                                    appExp.getValue().toString(), appExample.getValue().toString(), bigNameAmb));
                        } else if (appSection.getValue() != null && appSection.getValue().equals("Создание игр")) {
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                bigName += "...";
                            }
                            else
                                bigName = appName.getValue().toString();

                            if (appPurpose.getValue().toString().length() > 146) {
                                bigNameAmb = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigNameAmb += name.charAt(j);
                                }
                                bigNameAmb += "...";
                            }
                            else
                                bigNameAmb = appPurpose.getValue().toString();

                            appsGames.add(new AdapterElement("  " + bigName, appCreator.getValue().toString(), String.valueOf(appId),
                                    appExp.getValue().toString(), appExample.getValue().toString(), bigNameAmb));
                        } else if (appSection.getValue() != null && appSection.getValue().equals("Создание сайтов")) {
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                bigName += "...";
                            }
                            else
                                bigName = appName.getValue().toString();

                            if (appPurpose.getValue().toString().length() > 146) {
                                bigNameAmb = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigNameAmb += name.charAt(j);
                                }
                                bigNameAmb += "...";
                            }
                            else
                                bigNameAmb = appPurpose.getValue().toString();

                            appsSites.add(new AdapterElement("  " + bigName, appCreator.getValue().toString(), String.valueOf(appId),
                                    appExp.getValue().toString(), appExample.getValue().toString(), bigNameAmb));
                        } else if (appSection.getValue() != null && appSection.getValue().equals("Создание приложений")) {
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                bigName += "...";
                            }
                            else
                                bigName = appName.getValue().toString();

                            if (appPurpose.getValue().toString().length() > 146) {
                                bigNameAmb = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigNameAmb += name.charAt(j);
                                }
                                bigNameAmb += "...";
                            }
                            else
                                bigNameAmb = appPurpose.getValue().toString();

                            apps.add(new AdapterElement("  " + bigName, appCreator.getValue().toString(), String.valueOf(appId),
                                    appExp.getValue().toString(), appExample.getValue().toString(), bigNameAmb));
                        } else {
                            if (appName.getValue().toString().length() > 25) {
                                bigName = "";
                                name = appName.getValue().toString();
                                for (int j = 0; j < 25; j++) {
                                    bigName += name.charAt(j);
                                }
                                bigName += "...";
                            }
                            else
                                bigName = appName.getValue().toString();

                            if (appPurpose.getValue().toString().length() > 146) {
                                bigNameAmb = "";
                                name = appPurpose.getValue().toString();
                                for (int j = 0; j < 146; j++) {
                                    bigNameAmb += name.charAt(j);
                                }
                                bigNameAmb += "...";
                            }
                            else
                                bigNameAmb = appPurpose.getValue().toString();

                            appsOthers.add(new AdapterElement("  " + bigName, appCreator.getValue().toString(), String.valueOf(appId),
                                    appExp.getValue().toString(), appExample.getValue().toString(), bigNameAmb));
                        }
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
