package com.example.maxim.myproject.db;

import com.example.maxim.myproject.model.AppModel;

import java.util.List;

public interface FilterAppsResultListener {
    void onFilterAppsResult(List<AppModel> list);
}
