package com.example.maxim.myproject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainAdapter extends ArrayAdapter<AdapterElement> {

    public MainAdapter(Context context, AdapterElement[] arr) {
        super(context, R.layout.one_adapner, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final AdapterElement month = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.one_adapner, null);
        }

        // Заполняем адаптер
        ((TextView) convertView.findViewById(R.id.applName)).setText(month.mainName);
        ((TextView) convertView.findViewById(R.id.writeAdout)).setText(String.valueOf(month.ambition));
        ((TextView) convertView.findViewById(R.id.experience)).setText(String.valueOf(month.experience));
        ((TextView) convertView.findViewById(R.id.examp)).setText(String.valueOf(month.example));
        ((TextView) convertView.findViewById(R.id.userBtn)).setText(String.valueOf(month.user));

        final ImageButton star = convertView.findViewById(R.id.imageButton0);
        final boolean[] h = {false};
        View.OnClickListener oclBtn3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!h[0]){
                    star.setImageResource(android.R.drawable.btn_star_big_on);
                    h[0] = true;
                }
                else{
                    star.setImageResource(android.R.drawable.btn_star_big_off);
                    h[0] = false;
                }
            }
        };
        // присвоим обработчик кнопке OK (btnOk)
        star.setOnClickListener(oclBtn3);
        return convertView;
    }
}
