package com.example.maxim.myproject.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.maxim.myproject.db.util.CorrectDbHelper;
import com.example.maxim.myproject.model.AppModel;
import com.example.maxim.myproject.R;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AdapterViewHolder> {
    private String userId;
    private boolean starFlag = false;
    private UserActionListener listener;
    private String userI = "";

    public AppsAdapter(String id) {
        userId = id;
    }

    // локальная версия списка
    private List<com.example.maxim.myproject.model.AppModel> mApps = new ArrayList<>();

    /**
     * Обновление списка, при получение новых данных
     *
     * @param newList - новый список
     */
    public void upDateData(List<com.example.maxim.myproject.model.AppModel> newList) {
        // обновляем данные
        mApps.clear();
        mApps.addAll(newList);

        // оповещаем адаптер об изменениях
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_adapter2, parent, false);
        return new AdapterViewHolder(view);
    }

    public void setUserActionListener(AppsAdapter.UserActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterViewHolder holder, int position) {
        AppModel app = mApps.get(position);
        holder.experience.setText(app.experience);
        holder.example.setText(app.example);
        holder.user.setText(app.creator);
        holder.appId.setText(app.appId);
        holder.mainName.setText(app.name);
        holder.ambition.setText(app.purpose);

        View.OnClickListener oclStarOnOff = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!starFlag) {
                    holder.star.setImageResource(android.R.drawable.btn_star_big_on);
                    CorrectDbHelper.getDatabase().child("users").child(userId).child("favourites")
                            .child("favourite" + holder.appId.getText().toString()).setValue("true");
                    starFlag = true;
                } else {
                    holder.star.setImageResource(android.R.drawable.btn_star_big_off);
                    CorrectDbHelper.getDatabase().child("users").child(userId).child("favourites")
                            .child("favourite" + holder.appId.getText().toString()).removeValue();
                    starFlag = false;
                }
            }
        };
        // присвоим обработчик кнопке
        holder.star.setOnClickListener(oclStarOnOff);


        if (CorrectDbHelper.dataSnapshot.child("users").child(userId).child("favourites")
                .child("favourite" + holder.appId.getText().toString()).getValue() != null) {
            holder.star.setImageResource(android.R.drawable.btn_star_big_on);
        } else
            holder.star.setImageResource(android.R.drawable.btn_star_big_off);


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
                Iterable<DataSnapshot> snapshotIterable = CorrectDbHelper.dataSnapshot.child("users").getChildren();

                if (userI.equals("")) {
                    for (DataSnapshot aSnapshotIterable : snapshotIterable) {
                        if (CorrectDbHelper.dataSnapshot.child("users").child(aSnapshotIterable.getKey().toString())
                                .child("login").getValue().toString()
                                .equals(holder.user.getText().toString())) {
                            userI = aSnapshotIterable.getKey().toString();
                        }
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(CorrectDbHelper.dataSnapshot.child("users").child(userI).child("login").getValue().toString())
                        .setMessage(CorrectDbHelper.dataSnapshot.child("users").child(userI).child("description").getValue().toString())
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
        };
        holder.user.setOnClickListener(oclBtnUser);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView mainName;
        TextView appId;
        TextView example;
        TextView ambition;
        TextView experience;
        TextView more;

        Button user;
        ImageButton star;

        AdapterViewHolder(View view) {
            super(view);
            mainName = view.findViewById(R.id.applName);
            appId = view.findViewById(R.id.applicationID);
            ambition = view.findViewById(R.id.writeAdout);
            experience = view.findViewById(R.id.expView);

            example = view.findViewById(R.id.exampView);

            more = view.findViewById(R.id.buttonMore);
            star = view.findViewById(R.id.imageButton0);
            user = view.findViewById(R.id.userBtn);
        }
    }

    /**
     * Создаем интерфейс "слушателя", который будет реализовывать наше активити
     */
    public interface UserActionListener {
        public void onShowMoreClick(String applicationId);
    }
}
