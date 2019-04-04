package com.example.maxim.myproject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MyMonthAdapter extends ArrayAdapter<MyMonth> {

    public MyMonthAdapter(Context context, MyMonth[] arr) {
        super(context, R.layout.one_adapner, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final MyMonth month = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.one_adapner, null);
        }

// Заполняем адаптер
        ((TextView) convertView.findViewById(R.id.applName)).setText(month.month);
        ((TextView) convertView.findViewById(R.id.writeAdout)).setText(String.valueOf(month.temp));
        ((TextView) convertView.findViewById(R.id.experience)).setText(String.valueOf(month.days));
        return convertView;
    }
}
