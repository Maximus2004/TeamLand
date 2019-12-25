package com.example.maxim.myproject.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.maxim.myproject.db.util.CorrectDbHelper;

import com.example.maxim.myproject.MainAdapter;
import com.example.maxim.myproject.MainAdapterForOther;
import com.example.maxim.myproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements MainAdapter.UserActionListener, MainAdapterForOther.UserActionListener {
    Button btnLogin;
    boolean isUserRegistrated = true;
    EditText pass, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupButtonReg();
        setupButtonLogin();
    }

    private void setupButtonReg() {
        Button buttonRegistration = findViewById(R.id.buttonRegistration);
        View.OnClickListener oclBtnRegistr = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
            }
        };
        buttonRegistration.setOnClickListener(oclBtnRegistr);
    }

    private void setupButtonLogin() {
        View.OnClickListener oclBtnReg = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        checkingUser(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        showToast("Ошибка!", databaseError.toString());
                    }
                };
                CorrectDbHelper.getInstance().getDatabase().addValueEventListener(listener);
            }
        };
        btnLogin = findViewById(R.id.email_sign_in_button);
        btnLogin.setOnClickListener(oclBtnReg);
    }

    private void checkingUser(DataSnapshot dataSnapshot) {
        pass = findViewById(R.id.password);
        login = findViewById(R.id.email);
        final String passT = pass.getText().toString();
        final String loginT = login.getText().toString();


        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.child("users").getChildren();

        for (DataSnapshot aSnapshotIterable : snapshotIterable) {
            Toast.makeText(getApplicationContext(), aSnapshotIterable.getKey().toString(), Toast.LENGTH_SHORT).show();
            if (dataSnapshot.child("users").child(aSnapshotIterable.getKey().toString()).child("login").getValue() != null &&
                    dataSnapshot.child("users").child(aSnapshotIterable.getKey().toString()).child("login").getValue().toString().equals(loginT)
                    && dataSnapshot.child("users").child(aSnapshotIterable.getKey().toString()).child("password").getValue() != null
                    && dataSnapshot.child("users").child(aSnapshotIterable.getKey().toString()).child("password").getValue().equals(passT)) {
                // нашли совпадение, останавливаем цикл
                isUserRegistrated = false;
                Intent intent = new Intent(LoginActivity.this, LeastMainActivity.class);
                intent.putExtra(LeastMainActivity.PARAM_USER_ID, aSnapshotIterable.getKey().toString());
                startActivity(intent);
                showToast("Вход успешно выполнен", "");
                break;
            }
        }
        if (isUserRegistrated) {
            showToast("Вы не подключены к сети или такого пользователя не существует", "");
        }
    }

    private void showToast(String text, String databaseError) {
        Toast toast = Toast.makeText(getApplicationContext(),
                text + databaseError + "", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onShowMoreClick(String applicationId) {

    }
}