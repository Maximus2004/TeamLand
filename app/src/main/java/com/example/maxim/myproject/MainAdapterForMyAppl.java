package com.example.maxim.myproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainAdapterForMyAppl extends RecyclerView.Adapter<MainAdapterForMyAppl.AdapterViewHolder> {
    private ArrayList<AppModel> apps;
    private DatabaseReference mDatabase;
    private String userI;
    UserActionListener listener;
    private String userId;
    private boolean starFlag = false;
    String applicationID = "";
    View viewPublic;

    public MainAdapterForMyAppl(ArrayList<AppModel> apps, String id) {
        this.apps = apps;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //здесь будет код
        userId = id;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView mainName;
        TextView appId;
        TextView example;
        TextView ambition;
        TextView experience;

        Button more;
        Button user;
        ImageButton star;

        LinearLayout layoutOneAdapter;
        LinearLayout tab1;

        public AdapterViewHolder(View view) {
            super(view);
            mainName = view.findViewById(R.id.applName);
            appId = view.findViewById(R.id.applicationID);
            ambition = view.findViewById(R.id.writeAdout);
            experience = view.findViewById(R.id.expView);

            example = view.findViewById(R.id.exampView);

            more = view.findViewById(R.id.buttonMore);
            star = view.findViewById(R.id.imageButton0);
            user = view.findViewById(R.id.userBtn);

            layoutOneAdapter = view.findViewById(R.id.layoutOneAdapter);

            tab1 = view.findViewById(R.id.tab1);
        }
    }

    public void setUserActionListener(MainAdapterForMyAppl.UserActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_adapter_for_myappl, parent, false);
        viewPublic = view;
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterViewHolder holder, int position) {
        String ambT = holder.ambition.getText().toString();
        Log.d("LENGTH", String.valueOf(ambT.length()));

        holder.experience.setText(apps.get(position).experience);

        String textExp = viewPublic.getResources().getQuantityString(R.plurals.plurals,
                Integer.parseInt(apps.get(position).experience), Integer.parseInt(apps.get(position).experience));
        holder.experience.setText(textExp);

        holder.example.setText(apps.get(position).example);
        holder.user.setText(apps.get(position).creator);
        holder.appId.setText(apps.get(position).applicationId);
        holder.mainName.setText(apps.get(position).name);
        holder.ambition.setText(apps.get(position).purpose);

        View.OnClickListener oclBtn3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueEventListener listenerAtOnceStar = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!starFlag) {
                            // да, здесь всё так же работает на maxId(ещё не поменял)
                            // (для этого проекта сущствует своя собственная база, которая ещё не отредактирована)
                            holder.star.setImageResource(android.R.drawable.btn_star_big_on);
                            mDatabase.child("users").child(userId).child("favourites").child("favourite" + holder.appId.getText().toString()).setValue("true");
                            starFlag = true;
                        } else {
                            holder.star.setImageResource(android.R.drawable.btn_star_big_off);
                            mDatabase.child("users").child(userId).child("favourites").child("favourite" + holder.appId.getText().toString()).removeValue();
                            starFlag = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.addListenerForSingleValueEvent(listenerAtOnceStar);
            }
        };
        // присвоим обработчик кнопке
        holder.star.setOnClickListener(oclBtn3);


        ValueEventListener listenerAtOnceStarOnOff = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("users").child(userId).child("favourites").child("favourite" + holder.appId.getText().toString()).getValue() != null) {
                    holder.star.setImageResource(android.R.drawable.btn_star_big_on);
                } else
                    holder.star.setImageResource(android.R.drawable.btn_star_big_off);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(listenerAtOnceStarOnOff);

        View.OnClickListener oclBtn0 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // если есть слушатель, передаем ему действие
                if (listener != null)
                    Log.d("APPID", holder.appId.getText().toString());
                    applicationID = holder.appId.getText().toString();
                    listener.onShowMoreClick(applicationID);
            }
        };
        holder.more.setOnClickListener(oclBtn0);

        View.OnClickListener oclBtnUser = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ValueEventListener listenerAtOnceUser = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (userId != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle(dataSnapshot.child("users").child(userId).child("login").getValue().toString())
                                    .setMessage(dataSnapshot.child("users").child(userId).child("description").getValue().toString())
                                    .setCancelable(false)
                                    .setNegativeButton("Понятно",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert2 = builder.create();
                            alert2.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //Toast.makeText(getContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
                    }
                };
                mDatabase.addListenerForSingleValueEvent(listenerAtOnceUser);
            }
        };
        holder.user.setOnClickListener(oclBtnUser);

    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    /**
     * Создаем интерфейс "слушателя", который будет реализовывать наше активити
     */
    public interface UserActionListener {
        public void onShowMoreClick(String applicationId);
    }
}


