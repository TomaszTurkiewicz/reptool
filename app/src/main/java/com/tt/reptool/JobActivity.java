package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private ManagerSpinnerAdapter managerSpinnerAdapter;
    private Manager pm = new Manager();
    Job job = new Job();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        jobNumber = findViewById(R.id.jobNumber);
        jobClientName = findViewById(R.id.clientName);
        jobStreet = findViewById(R.id.street);
        jobPostcode = findViewById(R.id.postcode);
        jobDescription = findViewById(R.id.jobDescription);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceJob=firebaseDatabase.getReference("Job");
        databaseReferenceManager = firebaseDatabase.getReference("Manager");

        mList.clear();
        databaseReferenceManager.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){
                    Manager manager = ps.getValue(Manager.class);
                    mList.add(manager);
                }
                jobPMSpinner = (Spinner)findViewById(R.id.jobPM);
                managerSpinnerAdapter = new ManagerSpinnerAdapter(JobActivity.this,mList);
                jobPMSpinner.setAdapter(managerSpinnerAdapter);
                jobPMSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Manager clickedItem = (Manager) parent.getItemAtPosition(position);
                        pm.setName(clickedItem.getName());
                        pm.setSurname(clickedItem.getSurname());
                        pm.setEmailAddress(clickedItem.getEmailAddress());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




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



        job.setJobNumber(jNumber);
        job.setAddress(jAddress);
        job.setShortDescription(jDescription);
        job.setProjectManager(pm);

        databaseReferenceJob.child(jNumber).setValue(job);
        jobNumber.setText("");
        jobClientName.setText("");
        jobStreet.setText("");
        jobPostcode.setText("");
        jobDescription.setText("");

    }

}
//TODO why doesn't add manager to job?
//TODO empty fields job
