package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.tt.reptool.javaClasses.JobType;
import com.tt.reptool.javaClasses.Manager;

import java.util.ArrayList;
import java.util.List;

public class JobActivity extends AppCompatActivity {

    private RadioButton instRadioButton;


    private EditText jobNumber;
    private EditText jobClientName;
    private EditText jobStreet;
    private EditText jobPostcode;
    private EditText jobDescription;
    private Spinner jobPMSpinner;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceJob;
    private DatabaseReference databaseReferenceJobMaintenance;
    private DatabaseReference databaseReferenceJobService;
    private DatabaseReference databaseReferenceJobCallOut;
    private DatabaseReference databaseReferenceManager;
    private List<Manager> mList = new ArrayList<>();
    private ManagerSpinnerAdapter managerSpinnerAdapter;
    private Manager pm = new Manager();
//    Job job = new Job();
    private LinearLayout pmLinearLayout;
    private LinearLayout jobNumberLinearLayout;
    private JobType jobType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        jobNumber = findViewById(R.id.jobNumber);
        jobClientName = findViewById(R.id.clientName);
        jobStreet = findViewById(R.id.street);
        jobPostcode = findViewById(R.id.postcode);
        jobDescription = findViewById(R.id.jobDescription);
        instRadioButton = findViewById(R.id.instRadioButton);
        pmLinearLayout = findViewById(R.id.pmLinearLayout);
        jobNumberLinearLayout = findViewById(R.id.jobNumberLinearLayout);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceJob=firebaseDatabase.getReference(getString(R.string.firebasepath_job));
        databaseReferenceJobService=firebaseDatabase.getReference(getString(R.string.firebasepath_job_service));
        databaseReferenceJobMaintenance=firebaseDatabase.getReference(getString(R.string.firebasepath_job_maintenance));
        databaseReferenceJobCallOut=firebaseDatabase.getReference(getString(R.string.firebasepath_job_callout));
        databaseReferenceManager = firebaseDatabase.getReference(getString(R.string.firebasepath_manager));

        instRadioButton.setChecked(true);
        jobType=JobType.INSTALLATION;


        //initialize list
        mList.clear();
        mList.add(null);
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
                        if(clickedItem!=null){
                        pm.setName(clickedItem.getName());
                        pm.setSurname(clickedItem.getSurname());
                        pm.setEmailAddress(clickedItem.getEmailAddress());
                    }
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

        //check empty fields: job - number,postcode,street,description,manager

        if (jobType==JobType.INSTALLATION){
            if (!TextUtils.isEmpty(jNumber) &&
                    !TextUtils.isEmpty(jAddress.getName())&&
                    !TextUtils.isEmpty(jAddress.getPostCode()) &&
                    !TextUtils.isEmpty(jAddress.getStreet()) &&
                    !TextUtils.isEmpty(jDescription) &&
                    pm.getName() != null) {
                final Job job = new Job();
                job.setJobNumber(jNumber);
                job.setAddress(jAddress);
                job.setShortDescription(jDescription);
                job.setProjectManager(pm);
                job.setJobType(jobType);
                databaseReferenceJob.child(job.getJobNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //check if job (with this number) already exists
                        if (dataSnapshot.exists()) {
                            Toast.makeText(JobActivity.this, getString(R.string.job_exists),
                                    Toast.LENGTH_LONG).show();
                        } else {


                            databaseReferenceJob.child(job.getJobNumber()).setValue(job);
                            jobNumber.setText(getString(R.string.empty));
                            jobClientName.setText(getString(R.string.empty));
                            jobStreet.setText(getString(R.string.empty));
                            jobPostcode.setText(getString(R.string.empty));
                            jobDescription.setText(getString(R.string.empty));
                            jobPMSpinner.setAdapter(managerSpinnerAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                Toast.makeText(JobActivity.this, getString(R.string.empty_fields),
                        Toast.LENGTH_LONG).show();
            }
    }
        else{
            if (!TextUtils.isEmpty(jAddress.getName()) &&
                    !TextUtils.isEmpty(jAddress.getPostCode()) &&
                    !TextUtils.isEmpty(jAddress.getStreet()) &&
                    !TextUtils.isEmpty(jDescription)) {
  //              job.setJobNumber(jNumber);
                final Job job = new Job();
                job.setAddress(jAddress);
                job.setShortDescription(jDescription);
                job.setJobType(jobType);
  //              job.setProjectManager(pm);

                if(jobType==JobType.MAINTENANCE){
                    databaseReferenceJobMaintenance.child(job.getAddress().getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //check if job (with this number) already exists
                            if (dataSnapshot.exists()) {
                                Toast.makeText(JobActivity.this, getString(R.string.job_exists),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                databaseReferenceJobMaintenance.child(job.getAddress().getName()).setValue(job);
                                jobNumber.setText(getString(R.string.empty));
                                jobClientName.setText(getString(R.string.empty));
                                jobStreet.setText(getString(R.string.empty));
                                jobPostcode.setText(getString(R.string.empty));
                                jobDescription.setText(getString(R.string.empty));
                                jobPMSpinner.setAdapter(managerSpinnerAdapter);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }

                else if(jobType==JobType.SERVICE){
                    databaseReferenceJobService.child(job.getAddress().getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //check if job (with this number) already exists
                            if (dataSnapshot.exists()) {
                                Toast.makeText(JobActivity.this, getString(R.string.job_exists),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                databaseReferenceJobService.child(job.getAddress().getName()).setValue(job);
                                jobNumber.setText(getString(R.string.empty));
                                jobClientName.setText(getString(R.string.empty));
                                jobStreet.setText(getString(R.string.empty));
                                jobPostcode.setText(getString(R.string.empty));
                                jobDescription.setText(getString(R.string.empty));
                                jobPMSpinner.setAdapter(managerSpinnerAdapter);

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }

                else if(jobType==JobType.CALL_OUT){
                    databaseReferenceJobCallOut.child(job.getAddress().getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //check if job (with this number) already exists
                            if (dataSnapshot.exists()) {
                                Toast.makeText(JobActivity.this, getString(R.string.job_exists),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                databaseReferenceJobCallOut.child(job.getAddress().getName()).setValue(job);
                                jobNumber.setText(getString(R.string.empty));
                                jobClientName.setText(getString(R.string.empty));
                                jobStreet.setText(getString(R.string.empty));
                                jobPostcode.setText(getString(R.string.empty));
                                jobDescription.setText(getString(R.string.empty));
                                jobPMSpinner.setAdapter(managerSpinnerAdapter);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }

            } else {
                Toast.makeText(JobActivity.this, getString(R.string.empty_fields),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton)view).isChecked();

        switch (view.getId()){
            case R.id.instRadioButton:
                if(checked){
                   pmLinearLayout.setVisibility(View.VISIBLE);
                   jobNumberLinearLayout.setVisibility(View.VISIBLE);
                   jobType=JobType.INSTALLATION;
                }
                break;

            case R.id.maintRadioButton:
                if(checked){
                    pmLinearLayout.setVisibility(View.GONE);
                    jobNumberLinearLayout.setVisibility(View.GONE);
                    jobType=JobType.MAINTENANCE;
                }
                break;

            case R.id.servRadioButton:
                if(checked){
                    pmLinearLayout.setVisibility(View.GONE);
                    jobNumberLinearLayout.setVisibility(View.GONE);
                    jobType=JobType.SERVICE;
                }
                break;

            case R.id.callRadioButton:
                if(checked){
                    pmLinearLayout.setVisibility(View.GONE);
                    jobNumberLinearLayout.setVisibility(View.GONE);
                    jobType=JobType.CALL_OUT;
                }
                break;
        }
    }
}

