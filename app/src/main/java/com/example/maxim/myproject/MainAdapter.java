package com.example.maxim.myproject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
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

        //как индивидуально обработать нажатие на кнопку

// Заполняем адаптер
        ((TextView) convertView.findViewById(R.id.applName)).setText(month.month);
        ((TextView) convertView.findViewById(R.id.writeAdout)).setText(String.valueOf(month.temp));
        ((TextView) convertView.findViewById(R.id.experience)).setText(String.valueOf(month.days));
        ((TextView) convertView.findViewById(R.id.examp)).setText(String.valueOf(month.example));

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
