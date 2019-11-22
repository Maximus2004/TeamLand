package com.example.maxim.myproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class moreAboutApplication extends AppCompatActivity {
    public String mainapplicationID;
    TextView applicationDescription, name, purpose, can, experience, hashs, example, sector, phone, vk, otherContacts;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_about_application);

        Intent intent = getIntent();
        mainapplicationID = intent.getStringExtra("applId");

        Toolbar toolbarMore = findViewById(R.id.toolbarMore);
        setSupportActionBar(toolbarMore);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Подробнее...");

        ValueEventListener listenerAtOnce = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name = findViewById(R.id.applicationName);
                applicationDescription = findViewById(R.id.descriptionApplication);
                purpose = findViewById(R.id.purposeApplication);
                can = findViewById(R.id.canApplication);
                experience = findViewById(R.id.experienceApplication);
                hashs = findViewById(R.id.hashtegsApplication);
                example = findViewById(R.id.exampleApplication);
                sector = findViewById(R.id.sectorApplication);
                phone = findViewById(R.id.phoneApplication);
                vk = findViewById(R.id.vkApplication);
                otherContacts = findViewById(R.id.otherContactsApplication);
                DataSnapshot app = dataSnapshot.child("applications").child(mainapplicationID);
                if (app.getValue() != null) {
                    name.setText(app.child("name").getValue().toString());
                    applicationDescription.setText(app.child("descriptionApplication").getValue().toString());
                    purpose.setText(app.child("purpose").getValue().toString());
                    can.setText(app.child("can").getValue().toString());
                    String experienceS = app.child("experience").getValue().toString();
                    String exp = getResources().getQuantityString(R.plurals.plurals, Integer.parseInt(experienceS), Integer.parseInt(experienceS));
                    experience.setText(exp);

                    if (app.child("hashs").getValue() != null) {
                        hashs.setText(app.child("hashs").getValue().toString());
                    } else
                        hashs.setText("");
                    example.setText(app.child("example").getValue().toString());
                    sector.setText(app.child("section").getValue().toString());
                    phone.setText(app.child("phone").getValue().toString());
                    vk.setText(app.child("vk").getValue().toString());
                    otherContacts.setText(app.child("other").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
