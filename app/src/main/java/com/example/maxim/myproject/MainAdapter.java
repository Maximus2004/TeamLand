package com.example.maxim.myproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.AdapterViewHolder> {
    private ArrayList<AppModel> apps;
    private DatabaseReference mDatabase;
    private String userI;
    UserActionListener listener;
    private String userId;
    private boolean starFlag = false;
    View viewPublic;

    public MainAdapter(ArrayList<AppModel> apps, String id) {
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

        LinearLayout tab1;

        public AdapterViewHolder(View view) {
            super(view);
            viewPublic = view;
            mainName = view.findViewById(R.id.applName);
            appId = view.findViewById(R.id.applicationID);
            ambition = view.findViewById(R.id.writeAdout);
            experience = view.findViewById(R.id.expView);

            example = view.findViewById(R.id.exampView);

            more = view.findViewById(R.id.buttonMore);
            star = view.findViewById(R.id.imageButton0);
            user = view.findViewById(R.id.userBtn);

            tab1 = view.findViewById(R.id.tab1);
        }
    }

    public void setUserActionListener(MainAdapter.UserActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_adapter2, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterViewHolder holder, int position) {
        String ambT = holder.ambition.getText().toString();
        Log.d("LENGTH", String.valueOf(ambT.length()));

        // устанавливаю чёрный цвет
        final ForegroundColorSpan styleExp = new ForegroundColorSpan(Color.rgb(0, 0, 0));
        // идентефицирую стринговую переемнную, оторая умеет работать со стилем выше
        //final SpannableStringBuilder textExp;

        // это то, что вызывает многократное добавление "лет" к каждой из заявок. Также получается так, то везде стоит "0"
        // беру текст из ЕditText experience и определяю, какая там цифра. В зависимости от цифры добаляю определённое слово
        holder.experience.setText(apps.get(position).experience);

        String textExp = viewPublic.getResources().getQuantityString(R.plurals.plurals, Integer.parseInt(apps.get(position).experience), Integer.parseInt(apps.get(position).experience));
        holder.experience.setText(textExp);

        // это, казалось бы, нормально работает, но во всех заявках появляется одинаковый статус налиция опыта
        // здесь ситуация обстоит так же, но теперь закрашивается слово "есть" или "нет", опять же чтобы было легко ориентироваться
        //holder.experience.setText(apps.get(position).getExperience());
        // с этим всё ок
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
                    listener.onShowMoreClick(holder.appId.getText().toString());
            }
        };
        holder.more.setOnClickListener(oclBtn0);

        View.OnClickListener oclBtnUser = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ValueEventListener listenerAtOnceUser = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.child("users").getChildren();

                        for (DataSnapshot aSnapshotIterable : snapshotIterable) {
                            if (dataSnapshot.child("users").child(aSnapshotIterable.getKey().toString()).child("login").getValue().toString()
                                    .equals(holder.user.getText().toString())) {
                                userI = aSnapshotIterable.getKey().toString();
                            }
                        }
                        if (userI != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle(dataSnapshot.child("users").child(userI).child("login").getValue().toString())
                                    .setMessage(dataSnapshot.child("users").child(userI).child("description").getValue().toString())
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


