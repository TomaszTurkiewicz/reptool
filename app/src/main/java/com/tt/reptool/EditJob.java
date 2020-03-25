package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.tt.reptool.adapters.ManagerSpinnerAdapter;
import com.tt.reptool.javaClasses.Address;
import com.tt.reptool.javaClasses.Job;
import com.tt.reptool.javaClasses.Manager;

import java.util.ArrayList;
import java.util.List;

public class EditJob extends AppCompatActivity {

    private EditText jobNumber, jobClientName, jobStreet, jobPostcode, jobDescription;
    private Spinner jobManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceManager;
    private ManagerSpinnerAdapter managerSpinnerAdapter;
    private Manager oManager = new Manager();
    private Address oAddress = new Address();
    private Job oJob = new Job();
    private Manager nManager = new Manager();
    private Address nAddress = new Address();
    private Job nJob = new Job();
    String jNumber;
    private List<Manager> managerList = new ArrayList<>();

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
        jobManager = (Spinner)findViewById(R.id.jobPMSpinner);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(getString(R.string.firebasepath_job));
        databaseReferenceManager = firebaseDatabase.getReference(getString(R.string.firebasepath_manager));
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
        managerList.clear();
        managerList.add(oManager);
        databaseReferenceManager.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps: dataSnapshot.getChildren()){
                    Manager manager = ps.getValue(Manager.class);
                    managerList.add(manager);
                }
                managerSpinnerAdapter = new ManagerSpinnerAdapter(EditJob.this, managerList);
                jobManager.setAdapter(managerSpinnerAdapter);
                jobManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Manager clickedItem = (Manager)parent.getItemAtPosition(position);
                        nManager.setName(clickedItem.getName());
                        nManager.setSurname(clickedItem.getSurname());
                        nManager.setEmailAddress(clickedItem.getEmailAddress());
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
    private void show(Job job) {
        jobNumber.setText(job.getJobNumber());
        jobDescription.setText(job.getShortDescription());
        jobClientName.setText(job.getAddress().getName());
        jobPostcode.setText(job.getAddress().getPostCode());
        jobStreet.setText(job.getAddress().getStreet());

    }

    public void onClickCancel(View view) {
        Intent intent = new Intent(this, FindJobActivity.class);
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

//        nManager.setName(oManager.getName());
//        nManager.setSurname(oManager.getSurname());
//        nManager.setEmailAddress(oManager.getEmailAddress());
        nJob.setProjectManager(nManager);

        databaseReference.child(nJob.getJobNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    databaseReference.child(oJob.getJobNumber()).setValue(oJob);
                    Toast.makeText(EditJob.this,getString(R.string.already_exists),Toast.LENGTH_LONG).show();
                }
                else {
                    databaseReference.child(nJob.getJobNumber()).setValue(nJob);
                    Intent i = new Intent(getApplicationContext(),FindJobActivity.class);
                    startActivity(i);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
