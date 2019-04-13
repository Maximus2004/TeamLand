package com.example.maxim.myproject;

//@IgnoreExtraProperties
public class User {
    public String firstName;
    public String lastName;
    public String userName;
    public String phone;
    public String gender;
    public int age;

    public User() {
    }

    public User(String firstName, String lastName, String userName, String phone, String gender, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
    }
}

