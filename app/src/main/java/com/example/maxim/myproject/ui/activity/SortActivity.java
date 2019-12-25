package com.example.maxim.myproject.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import com.example.maxim.myproject.R;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.maxim.myproject.ui.fragment.AppsListFragment;

import java.util.ArrayList;
import java.util.List;

public class SortActivity extends AppCompatActivity {
    private CheckBox exampleCheckBox, phoneCheckBox;
    private EditText exp;
    private Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        Toolbar toolbarSort = findViewById(R.id.toolbarSort);
        setSupportActionBar(toolbarSort);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Сортировка");
        exampleCheckBox = (CheckBox) findViewById(R.id.exampleCheckBox);
        phoneCheckBox = (CheckBox) findViewById(R.id.phoneCheckBox);
        exp = (EditText) findViewById(R.id.editTextExp);
        ok = (Button) findViewById(R.id.buttonOk);

        final boolean[] chosen = new boolean[3];
        String expText = exp.getText().toString();
        if (exampleCheckBox.isChecked())
            chosen[0] = true;
        else
            chosen[0] = false;
        if (phoneCheckBox.isChecked())
            chosen[1] = true;
        else
            chosen[1] = false;
        if (expText.equals("0"))
            chosen[2] = true;
        else
            chosen[2] = false;

        //final Pair<boolean[], String> pair = new Pair<>(chosen, expText);
        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SortActivity.this, LeastMainActivity.class);
                intent.putExtra(LeastMainActivity.PARAM_SORT, chosen);
                intent.putExtra(LeastMainActivity.PARAM_USER_ID, "59b60345-7d0e-4cf2-b011-6851c034e630");
                startActivity(intent);
            }
        };
        ok.setOnClickListener(oclBtnOk);
    }
}
