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
    private Manager oManager = new Manager();
    private Address oAddress = new Address();
    private Job oJob = new Job();
    private Manager nManager = new Manager();
    private Address nAddress = new Address();
    private Job nJob = new Job();
    String jNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);
        Bundle extras = getIntent().getExtras();
        jNumber = extras.getString(getString(R.string.extra_jobNumber));

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
                    oJob.setJobNumber(dataSnapshot
                            .child(getString(R.string.firebasepath_job_jobNumber))
                            .getValue()
                            .toString());

                    oJob.setShortDescription(dataSnapshot
                            .child(getString(R.string.firebasepath_job_description))
                            .getValue()
                            .toString());

                    oAddress.setName(dataSnapshot
                            .child(getString(R.string.firebasepath_job_address))
                            .child(getString(R.string.firebasepath_job_address_name))
                            .getValue()
                            .toString());

                    oAddress.setPostCode(dataSnapshot
                            .child(getString(R.string.firebasepath_job_address))
                            .child(getString(R.string.firebasepath_job_address_postcode))
                            .getValue()
                            .toString());

                    oAddress.setStreet(dataSnapshot
                            .child(getString(R.string.firebasepath_job_address))
                            .child(getString(R.string.firebasepath_job_address_street))
                            .getValue()
                            .toString());

                    oManager.setName(dataSnapshot
                            .child(getString(R.string.firebasepath_job_projectManager))
                            .child(getString(R.string.firebasepath_manager_name))
                            .getValue()
                            .toString());

                    oManager.setSurname(dataSnapshot
                            .child(getString(R.string.firebasepath_job_projectManager))
                            .child(getString(R.string.firebasepath_manager_surname))
                            .getValue()
                            .toString());

                    oManager.setEmailAddress(dataSnapshot
                            .child(getString(R.string.firebasepath_job_projectManager))
                            .child(getString(R.string.firebasepath_manager_emailAddress))
                            .getValue()
                            .toString());

                    oJob.setProjectManager(oManager);
                    oJob.setAddress(oAddress);
                    show(oJob);
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
        nJob.setJobNumber(jobNumber.getText().toString().trim());
        nJob.setShortDescription(jobDescription.getText().toString().trim());
        nAddress.setName(jobClientName.getText().toString().trim());
        nAddress.setStreet(jobStreet.getText().toString().trim());
        nAddress.setPostCode(jobPostcode.getText().toString().trim());
        nJob.setAddress(nAddress);

        nManager.setName(oManager.getName());
        nManager.setSurname(oManager.getSurname());
        nManager.setEmailAddress(oManager.getEmailAddress());
        nJob.setProjectManager(nManager);

        databaseReference.child(nJob.getJobNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    databaseReference.child(oJob.getJobNumber()).setValue(oJob);
                }
                else {
                    databaseReference.child(nJob.getJobNumber()).setValue(nJob);
                }
                Intent i = new Intent(getApplicationContext(),AllJobs.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
