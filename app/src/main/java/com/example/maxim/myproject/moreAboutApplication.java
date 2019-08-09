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

    //public moreAboutApplication(String applicationId) {
      //  this.mainapplicationID = applicationId;
    //}

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
                name.setText(dataSnapshot.child("applications").child("application" + mainapplicationID).child("name").getValue().toString());
                applicationDescription.setText(dataSnapshot.child("applications").child("application" + mainapplicationID).child("descriptionApplication").getValue().toString());
                purpose.setText(dataSnapshot.child("applications").child("application" + mainapplicationID).child("purpose").getValue().toString());
                can.setText(dataSnapshot.child("applications").child("application" + mainapplicationID).child("can").getValue().toString());
                experience.setText(dataSnapshot.child("applications").child("application" + mainapplicationID).child("experience").getValue().toString());
                if (dataSnapshot.child("applications").child("application" + mainapplicationID).child("hashs").getValue() != null) {
                    hashs.setText(dataSnapshot.child("applications").child("application" + mainapplicationID).child("hashs").getValue().toString());
                }
                else
                    hashs.setText("");
                example.setText(dataSnapshot.child("applications").child("application" + mainapplicationID).child("example").getValue().toString());
                sector.setText(dataSnapshot.child("applications").child("application" + mainapplicationID).child("section").getValue().toString());
                phone.setText(dataSnapshot.child("applications").child("application" + mainapplicationID).child("phone").getValue().toString());
                vk.setText(dataSnapshot.child("applications").child("application" + mainapplicationID).child("vk").getValue().toString());
                otherContacts.setText(dataSnapshot.child("applications").child("application" + mainapplicationID).child("other").getValue().toString());
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
