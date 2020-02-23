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
        jNumber = extras.getString(getString(R.string.extra_jobNumber));
        manager = new Manager();
        address = new Address();
        jobNumber = findViewById(R.id.jobNumberEditJob);
        jobClientName = findViewById(R.id.clientNameEditJob);
        jobStreet = findViewById(R.id.streetEditJob);
        jobPostcode = findViewById(R.id.postcodeEditJob);
        jobDescription = findViewById(R.id.jobDescriptionEditJob);
        jobManager = findViewById(R.id.jobPMEditJob);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(getString(R.string.firebasepath_job));

        databaseReference.child(jNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    job.setJobNumber(dataSnapshot
                            .child(getString(R.string.firebasepath_job_jobNumber))
                            .getValue()
                            .toString());

                    job.setShortDescription(dataSnapshot
                            .child(getString(R.string.firebasepath_job_description))
                            .getValue()
                            .toString());

                    address.setName(dataSnapshot
                            .child(getString(R.string.firebasepath_job_address))
                            .child(getString(R.string.firebasepath_job_address_name))
                            .getValue()
                            .toString());

                    address.setPostCode(dataSnapshot
                            .child(getString(R.string.firebasepath_job_address))
                            .child(getString(R.string.firebasepath_job_address_postcode))
                            .getValue()
                            .toString());

                    address.setStreet(dataSnapshot
                            .child(getString(R.string.firebasepath_job_address))
                            .child(getString(R.string.firebasepath_job_address_street))
                            .getValue()
                            .toString());

                    manager.setName(dataSnapshot
                            .child(getString(R.string.firebasepath_job_projectManager))
                            .child(getString(R.string.firebasepath_manager_name))
                            .getValue()
                            .toString());

                    manager.setSurname(dataSnapshot
                            .child(getString(R.string.firebasepath_job_projectManager))
                            .child(getString(R.string.firebasepath_manager_surname))
                            .getValue()
                            .toString());

                    manager.setEmailAddress(dataSnapshot
                            .child(getString(R.string.firebasepath_job_projectManager))
                            .child(getString(R.string.firebasepath_manager_emailAddress))
                            .getValue()
                            .toString());

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

//TODO check if job after editing already exists