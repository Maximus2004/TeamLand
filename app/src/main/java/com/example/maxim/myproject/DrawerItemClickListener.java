package com.example.maxim.myproject;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DrawerItemClickListener implements ListView.OnItemClickListener {
    // так нельзя вызывать активити
    // здесь создается другая сущность
    Main2Activity main = new Main2Activity();
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        main.toast(position);
    }
}
