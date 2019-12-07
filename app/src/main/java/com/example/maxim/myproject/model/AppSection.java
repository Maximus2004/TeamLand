package com.example.maxim.myproject.model;

// enum - однородное перечисление
// всё это - переменные поля класса
public enum AppSection {
    ALL("Все"),
    APPS("Создание приложений"),
    BUSINESS("Оффлайн бизнес"),
    GAMES("Создание игр"),
    INTERNET("Бизнес в интернете"),
    SITES("Создание сайтов"),
    OTHER("Другое");

    private String name;

    AppSection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
