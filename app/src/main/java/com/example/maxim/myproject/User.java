package com.example.maxim.myproject;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String userId;
    public String login;
    public String password;
    public String description;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String login, String password, String description) {
        this.userId = userId;
        this.login = login;
        this.password = password;
        this.description = description;
    }

}


