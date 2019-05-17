package com.example.maxim.myproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainAdapter extends ArrayAdapter<AdapterElement> {
    DatabaseReference mDatabase;

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
        ((TextView) convertView.findViewById(R.id.applicationID)).setText(String.valueOf(month.applicationId));

        final ImageButton star = convertView.findViewById(R.id.imageButton0);
        final boolean[] h = {false};
        View.OnClickListener oclBtn3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!h[0]) {
                    star.setImageResource(android.R.drawable.btn_star_big_on);
                    h[0] = true;
                } else {
                    star.setImageResource(android.R.drawable.btn_star_big_off);
                    h[0] = false;

                }
            }
        };
        // присвоим обработчик кнопке OK (btnOk)
        star.setOnClickListener(oclBtn3);


        final Button more = convertView.findViewById(R.id.buttonMore);
        View.OnClickListener oclBtn0 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueEventListener listenerAtOnce = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Toast.makeText(getContext(), dataSnapshot.child("applications").child("application"+ month.applicationId).child("vk").getValue().toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Зашёл в onCancelled", Toast.LENGTH_SHORT).show();
                    }
                };

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
                Toast.makeText(getContext(), month.mainName, Toast.LENGTH_LONG).show();
            }
        };
        more.setOnClickListener(oclBtn0);
        return convertView;
    }
}
