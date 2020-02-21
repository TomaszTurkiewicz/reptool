package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JobActivity extends AppCompatActivity {

    private EditText jobNumber;
    private EditText jobClientName;
    private EditText jobStreet;
    private EditText jobPostcode;
    private EditText jobDescription;
    private Spinner jobPMSpinner;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceJob;
    private DatabaseReference databaseReferenceManager;
    private List<Manager> mList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        jobNumber = findViewById(R.id.jobNumber);
        jobClientName = findViewById(R.id.clientName);
        jobStreet = findViewById(R.id.street);
        jobPostcode = findViewById(R.id.postcode);
        jobDescription = findViewById(R.id.jobDescription);
        jobPMSpinner = findViewById(R.id.jobPM);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceJob=firebaseDatabase.getReference("Job");
        databaseReferenceManager = firebaseDatabase.getReference("Manager");
        initManagerList();


    }


    public void findJob(View view) {
    }

    public void saveJob(View view) {
        String jNumber = jobNumber.getText().toString().trim().toUpperCase();
        Address jAddress = new Address();
        jAddress.setName(jobClientName.getText().toString().trim());
        jAddress.setStreet(jobStreet.getText().toString().trim());
        jAddress.setPostCode(jobPostcode.getText().toString().trim().toUpperCase());
        String jDescription = jobDescription.getText().toString().trim();

        Job job = new Job();
        job.setJobNumber(jNumber);
        job.setAddress(jAddress);
        job.setShortDescription(jDescription);

        databaseReferenceJob.child(jNumber).setValue(job);

    }

    private void initManagerList() {

        mList.clear();
        databaseReferenceManager.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){
                    Manager manager = ps.getValue(Manager.class);
                    mList.add(manager);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
