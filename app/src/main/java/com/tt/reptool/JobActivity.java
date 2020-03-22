package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
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
        databaseReferenceJob=firebaseDatabase.getReference(getString(R.string.firebasepath_job));
        databaseReferenceManager = firebaseDatabase.getReference(getString(R.string.firebasepath_manager));


        //initialize list
        mList.clear();
        databaseReferenceManager.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){
                    Manager manager = ps.getValue(Manager.class);
                    mList.add(manager);
                }

                //after list is initialize load to spinner

                jobPMSpinner = (Spinner)findViewById(R.id.jobPM);
                managerSpinnerAdapter = new ManagerSpinnerAdapter(JobActivity.this,mList);
                jobPMSpinner.setAdapter(managerSpinnerAdapter);
                jobPMSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        //Item selected from spinner
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
        Intent intent = new Intent(this, FindJobActivity.class);
        startActivity(intent);
    }

    public void saveJob(View view) {
        final String jNumber = jobNumber.getText().toString().trim();
        final Address jAddress = new Address();
        jAddress.setName(jobClientName.getText().toString().trim());
        jAddress.setStreet(jobStreet.getText().toString().trim());
        jAddress.setPostCode(jobPostcode.getText().toString().trim());
        final String jDescription = jobDescription.getText().toString().trim();

        //check empty fields: job - number,postcode,street,description
        if(!TextUtils.isEmpty(jNumber)&&
                !TextUtils.isEmpty(jAddress.getPostCode())&&
                !TextUtils.isEmpty(jAddress.getStreet())&&
                !TextUtils.isEmpty(jDescription)) {
            job.setJobNumber(jNumber);
            job.setAddress(jAddress);
            job.setShortDescription(jDescription);
            job.setProjectManager(pm);

            databaseReferenceJob.child(job.getJobNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //check if job (with this number) already exists
                    if(dataSnapshot.exists()){
                        Toast.makeText(JobActivity.this,getString(R.string.job_exists),
                                Toast.LENGTH_LONG).show();
                    }
                    else{


                        databaseReferenceJob.child(job.getJobNumber()).setValue(job);
                        jobNumber.setText(getString(R.string.empty));
                        jobClientName.setText(getString(R.string.empty));
                        jobStreet.setText(getString(R.string.empty));
                        jobPostcode.setText(getString(R.string.empty));
                        jobDescription.setText(getString(R.string.empty));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        else{
            Toast.makeText(JobActivity.this,getString(R.string.empty_fields),
                    Toast.LENGTH_LONG).show();
        }
    }

}

//TODO spinner default null selection at position 0
