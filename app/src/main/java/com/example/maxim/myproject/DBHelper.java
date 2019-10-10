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
    final static AdapterElement[][] arrBuisness = new AdapterElement[1][];
    final static AdapterElement[][] arrInternet = new AdapterElement[1][];
    final static AdapterElement[][] arrGames = new AdapterElement[1][];
    final static AdapterElement[][] arrSites = new AdapterElement[1][];
    final static AdapterElement[][] arrApps = new AdapterElement[1][];
    final static AdapterElementOther[][] arrOther = new AdapterElementOther[1][1];

    public static void fillDataAllOther() {
        ValueEventListener listenerAtOnce = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

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

                String bigName, name, section, bigSection;

                DataSnapshot appTable = dataSnapshot.child("applications");
                int maxId = Integer.parseInt(appTable.child("maxId").getValue().toString());


                for (int appId = maxId; appId > -1; appId--) {
                    DataSnapshot app = appTable.child("application" + appId);
                    DataSnapshot appName = app.child("name");
                    DataSnapshot appPurpose = app.child("purpose");
                    DataSnapshot appExp = app.child("experience");
                    DataSnapshot appExample = app.child("example");
                    DataSnapshot appCreator = app.child("creator");
                    DataSnapshot appSection = app.child("section");

                    if (appName.getValue() != null) {

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
                arrInternet[0] = new AdapterElement[internetMainNames.size()];
                arrBuisness[0] = new AdapterElement[buisnessMainNames.size()];
                arrGames[0] = new AdapterElement[gamesMainNames.size()];
                arrSites[0] = new AdapterElement[sitesMainNames.size()];
                arrApps[0] = new AdapterElement[appsMainNames.size()];
                arrOther[0] = new AdapterElementOther[otherMainNames.size()];

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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
    }
}
