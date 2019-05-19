package com.example.maxim.myproject;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Application {

    public String applicationId;
    public String creator;
    public String example;
    public String experience;
    public String name;
    public String purpose;
    public String section;
    public String other;
    public String phone;
    public String vk;
    public String can;
    public String descriptionApplication;
    public String hashs;

    public Application() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Application(String applicationId, String creator, String example, String experience, String name, String purpose, String section, String other, String phone, String vk, String can, String descriptionApplication, String hashs) {
        this.applicationId = applicationId;
        this.creator = creator;
        this.example = example;
        this.experience = experience;
        this.name = name;
        this.purpose = purpose;
        this.section = section;
        this.other = other;
        this.phone = phone;
        this.can = can;
        this.vk = vk;
        this.descriptionApplication = descriptionApplication;
        this.hashs = hashs;
    }
}
