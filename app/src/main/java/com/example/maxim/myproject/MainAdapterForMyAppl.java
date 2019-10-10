package com.example.maxim.myproject;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class MainAdapterForMyAppl extends ArrayAdapter<AdapterElement> {
    DatabaseReference mDatabase;
    String userI;
    String userId;
    UserActionListener listener;
    boolean starFlag = false;

    public MainAdapterForMyAppl(Context context, AdapterElement[] arr, final String userName) {
        super(context, R.layout.one_adapter_for_myappl, arr);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userId = userName;
    }

    public void setUserActionListener(UserActionListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final AdapterElement month = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.one_adapter_for_myappl, null);
        }
        LinearLayout layoutOneAdapter = convertView.findViewById(R.id.layoutOneAdapter);
        ViewGroup.LayoutParams params = layoutOneAdapter.getLayoutParams();

        LinearLayout tab1 = convertView.findViewById(R.id.tab1);
        ViewGroup.LayoutParams paramsTab = tab1.getLayoutParams();

        TextView writeAbout = ((TextView) convertView.findViewById(R.id.writeAdout));
        ViewGroup.LayoutParams paramsTextView = writeAbout.getLayoutParams();

        // Заполняем адаптер
        if (month.ambition.length() < 118 && month.ambition.length() > 85) {
            params.height = (int) (layoutOneAdapter.getResources().getDisplayMetrics().density * 206);
            layoutOneAdapter.setLayoutParams(params);
            paramsTextView.height = (int) (writeAbout.getResources().getDisplayMetrics().density * 84);
            writeAbout.setLayoutParams(paramsTextView);
            paramsTab.height = (int) (tab1.getResources().getDisplayMetrics().density * 221);
            tab1.setLayoutParams(paramsTab);
        } else if (month.ambition.length() < 85 && month.ambition.length() > 61) {
            params.height = (int) (layoutOneAdapter.getResources().getDisplayMetrics().density * 189);
            layoutOneAdapter.setLayoutParams(params);
            paramsTextView.height = (int) (writeAbout.getResources().getDisplayMetrics().density * 65);
            writeAbout.setLayoutParams(paramsTextView);
            paramsTab.height = (int) (tab1.getResources().getDisplayMetrics().density * 201);
            tab1.setLayoutParams(paramsTab);
        } else if (month.ambition.length() < 61 && month.ambition.length() > 30) {
            params.height = (int) (layoutOneAdapter.getResources().getDisplayMetrics().density * 168);
            layoutOneAdapter.setLayoutParams(params);
            paramsTextView.height = (int) (writeAbout.getResources().getDisplayMetrics().density * 44);
            writeAbout.setLayoutParams(paramsTextView);
            paramsTab.height = (int) (tab1.getResources().getDisplayMetrics().density * 181);
            tab1.setLayoutParams(paramsTab);
        } else if (month.ambition.length() < 30) {
            params.height = (int) (layoutOneAdapter.getResources().getDisplayMetrics().density * 158);
            layoutOneAdapter.setLayoutParams(params);
            paramsTextView.height = (int) (writeAbout.getResources().getDisplayMetrics().density * 34);
            writeAbout.setLayoutParams(paramsTextView);
            paramsTab.height = (int) (tab1.getResources().getDisplayMetrics().density * 175);
            tab1.setLayoutParams(paramsTab);
        } else {
            params.height = (int) (layoutOneAdapter.getResources().getDisplayMetrics().density * 226);
            layoutOneAdapter.setLayoutParams(params);
            paramsTextView.height = (int) (writeAbout.getResources().getDisplayMetrics().density * 105);
            writeAbout.setLayoutParams(paramsTextView);
            paramsTab.height = (int) (tab1.getResources().getDisplayMetrics().density * 242);
            tab1.setLayoutParams(paramsTab);
        }


        // Заполняем адаптер
        ((TextView) convertView.findViewById(R.id.applName)).setText(month.mainName);
        ((TextView) convertView.findViewById(R.id.writeAdout)).setText(String.valueOf(month.ambition));
        final ForegroundColorSpan styleExp = new ForegroundColorSpan(Color.rgb(0, 0, 0));
        final SpannableStringBuilder textExp;
        TextView textViewExp = (TextView) convertView.findViewById(R.id.experience);

        ((TextView) convertView.findViewById(R.id.applName)).setText(month.mainName);
        ((TextView) convertView.findViewById(R.id.writeAdout)).setText(String.valueOf(month.ambition));

        if (String.valueOf(month.experience).charAt(8) == '0' && String.valueOf(month.experience).length() < 10) {
            textExp = new SpannableStringBuilder(String.valueOf(month.experience) + " лет");
            textExp.setSpan(styleExp, 8, textExp.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            textViewExp.setText(textExp);
        } else if (String.valueOf(month.experience).charAt(8) == '1' && String.valueOf(month.experience).length() < 10) {
            textExp = new SpannableStringBuilder(String.valueOf(month.experience) + " год");
            textExp.setSpan(styleExp, 8, textExp.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            textViewExp.setText(textExp);
        } else if (String.valueOf(month.experience).charAt(8) == '2' && String.valueOf(month.experience).length() < 10 || String.valueOf(month.experience).charAt(8) == '3' && String.valueOf(month.experience).length() < 10 || String.valueOf(month.experience).charAt(8) == '4' && String.valueOf(month.experience).length() < 10) {
            textExp = new SpannableStringBuilder(String.valueOf(month.experience) + " года");
            textExp.setSpan(styleExp, 8, textExp.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            textViewExp.setText(textExp);
        } else {
            textExp = new SpannableStringBuilder(String.valueOf(month.experience) + " лет");
            textExp.setSpan(styleExp, 8, textExp.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            textViewExp.setText(textExp);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.examp);

        final SpannableStringBuilder text = new SpannableStringBuilder(String.valueOf(month.example));
        final ForegroundColorSpan style = new ForegroundColorSpan(Color.rgb(0, 0, 0));
        if (month.example.length() > 20) {
            text.setSpan(style, 17, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            textView.setText(text);
        }
        if (month.example.length() == 20) {
            text.setSpan(style, 17, 20, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            textView.setText(text);
        }
        ((TextView) convertView.findViewById(R.id.userBtn)).setText(String.valueOf(month.user));
        ((TextView) convertView.findViewById(R.id.applicationID)).setText(String.valueOf(month.applicationId));

        final ImageButton star = convertView.findViewById(R.id.imageButton0);
        View.OnClickListener oclBtn3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueEventListener listenerAtOnceStar = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!starFlag) {
                            star.setImageResource(android.R.drawable.btn_star_big_on);
                            mDatabase.child("users").child(userId).child("favourites").child("favourite" + month.applicationId).setValue("true");
                            starFlag = true;
                        } else {
                            star.setImageResource(android.R.drawable.btn_star_big_off);
                            mDatabase.child("users").child(userId).child("favourites").child("favourite" + month.applicationId).removeValue();
                            starFlag = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Зашёл в onCancelled", Toast.LENGTH_SHORT).show();
                    }
                };
                mDatabase.addListenerForSingleValueEvent(listenerAtOnceStar);
            }
        };
        // присвоим обработчик кнопке
        star.setOnClickListener(oclBtn3);

        ValueEventListener listenerAtOnceStarOnOff = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("users").child(userId).child("favourites").child("favourite" + month.applicationId).getValue() != null) {
                    star.setImageResource(android.R.drawable.btn_star_big_on);
                } else
                    star.setImageResource(android.R.drawable.btn_star_big_off);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(listenerAtOnceStarOnOff);

        final Button edit = convertView.findViewById(R.id.buttonEdit);
        View.OnClickListener oclBtn0 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // если есть слушатель, передаем ему действие
                if (listener != null)
                    listener.onShowMoreClick(month.applicationId);
            }
        };
        edit.setOnClickListener(oclBtn0);

        final Button user = convertView.findViewById(R.id.userBtn);
        View.OnClickListener oclBtnUser = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueEventListener listenerAtOnceUser = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.child("users").getChildren();

                        for (DataSnapshot aSnapshotIterable : snapshotIterable) {
                            //userMap.put(aSnapshotIterable.getKey().toString(), (User) aSnapshotIterable.child("login").getValue());
                            if (dataSnapshot.child("users").child(aSnapshotIterable.getKey().toString()).child("login").getValue().toString().equals(month.user)) {
                                userI = aSnapshotIterable.getKey().toString();
                            }
                        }
                        if (!userI.equals("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                        else{

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
                    }
                };

                mDatabase.addListenerForSingleValueEvent(listenerAtOnceUser);
            }
        };
        user.setOnClickListener(oclBtnUser);
        return convertView;
    }

    /**
     * Создаем интерфейс "слушателя", который будет реализовывать наше активити
     */
    public interface UserActionListener {
        public void onShowMoreClick(String applicationId);
    }
}

