package com.example.maxim.myproject;

public class AdapterElementOther {
    private String mainName = ""; // Название заявки
    private String ambition = ""; // Цель
    private String experience = ""; // Опыт
    private String example = ""; // Наличие примера
    private String user = ""; //Имя пользователя
    private String appID = ""; //и так понятно))
    private String sectionClass = ""; // секция/раздел

    public AdapterElementOther(String mainName, String user, String appID, String experience, String example, String ambition, String sectionClass) {
        this.mainName = mainName;
        this.user = user;
        this.appID = appID;
        this.experience = experience;
        this.example = example;
        this.ambition = ambition;
        this.sectionClass = sectionClass;
    }

    public String getMainName(){
        return mainName;
    }

    public String getUser(){
        return user;
    }

    public String getAppId(){
        return appID;
    }

    public String getAmbition(){
        return ambition;
    }

    public String getExperience(){
        return experience;
    }

    public String getExample(){
        return example;
    }

    public  String getSection() { return sectionClass; }
}
