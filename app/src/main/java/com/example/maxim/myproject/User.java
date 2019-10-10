package com.example.maxim.myproject;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String login;
    public String password;
    public String description;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String login, String password, String description) {
        this.login = login;
        this.password = password;
        this.description = description;
    }
}


