package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditJob extends AppCompatActivity {

    private EditText jobNumber, jobClientName, jobStreet, jobPostcode, jobDescription;
    private TextView jobManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Manager manager;
    private Address address;
    Job job = new Job();
    String jNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);
        Bundle extras = getIntent().getExtras();
        jNumber = extras.getString("jobNumber");
        manager = new Manager();
        address = new Address();
        jobNumber = findViewById(R.id.jobNumberEditJob);
        jobClientName = findViewById(R.id.clientNameEditJob);
        jobStreet = findViewById(R.id.streetEditJob);
        jobPostcode = findViewById(R.id.postcodeEditJob);
        jobDescription = findViewById(R.id.jobDescriptionEditJob);
        jobManager = findViewById(R.id.jobPMEditJob);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Job");

        databaseReference.child(jNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    job.setJobNumber(dataSnapshot.child("jobNumber").getValue().toString());
                    job.setShortDescription(dataSnapshot.child("shortDescription").getValue().toString());
                    address.setName(dataSnapshot.child("address").child("name").getValue().toString());
                    address.setPostCode(dataSnapshot.child("address").child("postCode").getValue().toString());
                    address.setStreet(dataSnapshot.child("address").child("street").getValue().toString());
                    manager.setName(dataSnapshot.child("projectManager").child("name").getValue().toString());
                    manager.setSurname(dataSnapshot.child("projectManager").child("surname").getValue().toString());
                    manager.setEmailAddress(dataSnapshot.child("projectManager").child("emailAddress").getValue().toString());
                    job.setProjectManager(manager);
                    job.setAddress(address);
                    show(job);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void show(Job job) {
        jobNumber.setText(job.getJobNumber());
        jobDescription.setText(job.getShortDescription());
        jobClientName.setText(job.getAddress().getName());
        jobPostcode.setText(job.getAddress().getPostCode());
        jobStreet.setText(job.getAddress().getStreet());
        jobManager.setText(job.getProjectManager().nameAndSurnameToString());
    }

    public void onClickCancel(View view) {
        Intent intent = new Intent(this, AllJobs.class);
        startActivity(intent);
        finish();
    }

    public void onClickSave(View view) {
        databaseReference.child(jNumber).removeValue();
        job.setJobNumber(jobNumber.getText().toString().trim());
        job.setShortDescription(jobDescription.getText().toString().trim());
        address.setName(jobClientName.getText().toString().trim());
        address.setStreet(jobStreet.getText().toString().trim());
        address.setPostCode(jobPostcode.getText().toString().trim());
        job.setAddress(address);
        databaseReference.child(job.getJobNumber()).setValue(job);
        Intent intent = new Intent(this, AllJobs.class);
        startActivity(intent);
        finish();
    }
}