package com.example.maxim.myproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Класс помощник по работе с SharedPreferences
 */
public class MySharedPrefs {

    private static final String APP_PREFERENCES = "ALL_ALL";

    /**
     * Приватный конструктор для запрета создания экземпляра класса
     */
    private MySharedPrefs() {
    }

    /**
     * Возвращает общие для приложения SharedPreferences
     *
     * @param context – activity, context или ApplicationContext
     */
    public static SharedPreferences getAppPreferences(Context context) {
        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }
}
