package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.tt.reptool.javaClasses.JobType;
import com.tt.reptool.javaClasses.Manager;

import java.util.ArrayList;
import java.util.List;

public class EditJob extends AppCompatActivity {

    private EditText jobNumber, jobClientName, jobStreet, jobPostcode, jobDescription;
    private Spinner jobManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceJob;
    private DatabaseReference databaseReferenceMaintenance;
    private DatabaseReference databaseReferenceService;
    private DatabaseReference databaseReferenceCallOut;
    private DatabaseReference databaseReferenceManager;
    private ManagerSpinnerAdapter managerSpinnerAdapter;
    private Manager oManager = new Manager();
    private Address oAddress = new Address();
    private Job oJob = new Job();
    private Manager nManager = new Manager();
    private Address nAddress = new Address();
    private Job nJob = new Job();
    private String jNumber;
    private String jName;
    private JobType jobType;
    private List<Manager> managerList = new ArrayList<>();
    private LinearLayout jNumberLinearLayout;
    private LinearLayout managerLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);
        Bundle extras = getIntent().getExtras();

        jobType = JobType.valueOf(extras.getString("jobType"));

        jobNumber = findViewById(R.id.jobNumberEditJob);
        jobClientName = findViewById(R.id.clientNameEditJob);
        jobStreet = findViewById(R.id.streetEditJob);
        jobPostcode = findViewById(R.id.postcodeEditJob);
        jobDescription = findViewById(R.id.jobDescriptionEditJob);
        jobManager = (Spinner) findViewById(R.id.jobPMSpinner);
        jNumberLinearLayout = findViewById(R.id.jobNumberLinearLayoutEditJob);
        managerLinearLayout = findViewById(R.id.managerLinearLayoutEditJob);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceManager = firebaseDatabase.getReference(getString(R.string.firebasepath_manager));
        databaseReferenceJob = firebaseDatabase.getReference(getString(R.string.firebasepath_job));
        databaseReferenceMaintenance = firebaseDatabase.getReference(getString(R.string.firebasepath_job_maintenance));
        databaseReferenceService = firebaseDatabase.getReference(getString(R.string.firebasepath_job_service));
        databaseReferenceCallOut = firebaseDatabase.getReference(getString(R.string.firebasepath_job_callout));

        switch(jobType){
            case INSTALLATION:{
                jNumber = extras.getString(getString(R.string.extra_jobNumber));

                databaseReferenceJob.child(jNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            oJob=dataSnapshot.getValue(Job.class);
                            oManager=oJob.getProjectManager();
                            oJob.setJobType(jobType);
                            show(oJob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                break;
            }

            case MAINTENANCE:{
                jName = extras.getString("jobName");

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReferenceMaintenance.child(jName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            oJob = dataSnapshot.getValue(Job.class);
                            show(oJob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
            case SERVICE:{
                jName = extras.getString("jobName");

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReferenceService.child(jName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            oJob = dataSnapshot.getValue(Job.class);
                            show(oJob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
            case CALL_OUT:{
                jName = extras.getString("jobName");

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReferenceCallOut.child(jName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            oJob = dataSnapshot.getValue(Job.class);
                            show(oJob);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
        }


        }
    private void show(Job job) {
        jobDescription.setText(job.getShortDescription());
        jobClientName.setText(job.getAddress().getName());
        jobPostcode.setText(job.getAddress().getPostCode());
        jobStreet.setText(job.getAddress().getStreet());


        switch (jobType){
            case INSTALLATION:{
                jobNumber.setText(job.getJobNumber());
                managerList.clear();
                databaseReferenceManager.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ps : dataSnapshot.getChildren()) {
                            Manager manager = ps.getValue(Manager.class);
                            managerList.add(manager);
                        }
                        managerSpinnerAdapter = new ManagerSpinnerAdapter(EditJob.this, managerList);
                        jobManager.setAdapter(managerSpinnerAdapter);
                        int position = -1;
                        for (int i = 0; i < managerList.size(); i++) {
                            if (managerList.get(i).getName().equals(oManager.getName())
                                    &&managerList.get(i).getSurname().equals(oManager.getSurname())) {
                                position = i;
                            }
                        }
                        jobManager.setSelection(position);

                        jobManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Manager clickedItem = (Manager) parent.getItemAtPosition(position);
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
                RadioButton r = findViewById(R.id.instRadioButtonEditJob);
                r.setChecked(true);
                break;

            }
            case MAINTENANCE:{
                jNumberLinearLayout.setVisibility(View.GONE);
                managerLinearLayout.setVisibility(View.GONE);
                RadioButton r = findViewById(R.id.maintRadioButtonEditJob);
                r.setChecked(true);
                break;
            }
            case SERVICE:{
                jNumberLinearLayout.setVisibility(View.GONE);
                managerLinearLayout.setVisibility(View.GONE);
                RadioButton r = findViewById(R.id.servRadioButtonEditJob);
                r.setChecked(true);
                break;
            }
            case CALL_OUT:{
                jNumberLinearLayout.setVisibility(View.GONE);
                managerLinearLayout.setVisibility(View.GONE);
                RadioButton r = findViewById(R.id.callRadioButtonEditJob);
                r.setChecked(true);
                break;
            }
        }





    }

    public void onClickCancel(View view) {
        Intent intent = new Intent(this, FindJobActivity.class);
        startActivity(intent);
        finish();
    }

    public void removeOldJob(){
        switch (oJob.getJobType()){
            case INSTALLATION:{
                databaseReferenceJob.child(oJob.getJobNumber()).removeValue();
                break;
            }
            case MAINTENANCE:{
                databaseReferenceMaintenance.child(oJob.getAddress().getName()).removeValue();
                break;
            }
            case SERVICE:{
                databaseReferenceService.child(oJob.getAddress().getName()).removeValue();
                break;
            }
            case CALL_OUT:{
                databaseReferenceCallOut.child(oJob.getAddress().getName()).removeValue();
                break;
            }
        }
    }

    public void createNewJobObject(){
        if (jobType == JobType.INSTALLATION) {
            nJob.setJobNumber(jobNumber.getText().toString().trim());
            nJob.setShortDescription(jobDescription.getText().toString().trim());
            nAddress.setName(jobClientName.getText().toString().trim());
            nAddress.setStreet(jobStreet.getText().toString().trim());
            nAddress.setPostCode(jobPostcode.getText().toString().trim());
            nJob.setAddress(nAddress);
            nJob.setProjectManager(nManager);
        } else {
            nJob.setShortDescription(jobDescription.getText().toString().trim());
            nAddress.setName(jobClientName.getText().toString().trim());
            nAddress.setStreet(jobStreet.getText().toString().trim());
            nAddress.setPostCode(jobPostcode.getText().toString().trim());
            nJob.setAddress(nAddress);
        }
        nJob.setJobType(jobType);
    }

    public void checkIfNewJobAlreadyExistsAndSave(){
        switch (jobType){
            case INSTALLATION:{

                databaseReferenceJob.child(nJob.getJobNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            saveOldJob();

                            Toast.makeText(EditJob.this,getString(R.string.already_exists),Toast.LENGTH_LONG).show();
                        }
                        else {
                            databaseReferenceJob.child(nJob.getJobNumber()).setValue(nJob);
                            Intent i = new Intent(getApplicationContext(),FindJobActivity.class);
                            startActivity(i);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            }
            case MAINTENANCE:{

                databaseReferenceMaintenance.child(nJob.getAddress().getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            saveOldJob();

                            Toast.makeText(EditJob.this,getString(R.string.already_exists),Toast.LENGTH_LONG).show();
                        }
                        else {
                            databaseReferenceMaintenance.child(nJob.getAddress().getName()).setValue(nJob);
                            Intent i = new Intent(getApplicationContext(),FindJobActivity.class);
                            startActivity(i);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            }
            case SERVICE:{

                databaseReferenceService.child(nJob.getAddress().getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            saveOldJob();

                            Toast.makeText(EditJob.this,getString(R.string.already_exists),Toast.LENGTH_LONG).show();
                        }
                        else {
                            databaseReferenceService.child(nJob.getAddress().getName()).setValue(nJob);
                            Intent i = new Intent(getApplicationContext(),FindJobActivity.class);
                            startActivity(i);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            }
            case CALL_OUT:{

                databaseReferenceCallOut.child(nJob.getAddress().getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            saveOldJob();

                            Toast.makeText(EditJob.this,getString(R.string.already_exists),Toast.LENGTH_LONG).show();
                        }
                        else {
                            databaseReferenceCallOut.child(nJob.getAddress().getName()).setValue(nJob);
                            Intent i = new Intent(getApplicationContext(),FindJobActivity.class);
                            startActivity(i);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            }
        }
    }

    private void saveOldJob() {
        switch (oJob.getJobType()){
            case INSTALLATION:{
                databaseReferenceJob.child(oJob.getJobNumber()).setValue(oJob);
                break;
            }
            case MAINTENANCE:{
                databaseReferenceMaintenance.child(oJob.getAddress().getName()).setValue(oJob);
                break;
            }
            case SERVICE:{
                databaseReferenceService.child(oJob.getAddress().getName()).setValue(oJob);
                break;
            }
            case CALL_OUT:{
                databaseReferenceCallOut.child(oJob.getAddress().getName()).setValue(oJob);
                break;
            }
        }
    }

    public void onClickSave(View view) {

        if(jobType==JobType.INSTALLATION){
            String jNumber = jobNumber.getText().toString().trim();
            Address jAddress = new Address();
            jAddress.setName(jobClientName.getText().toString().trim());
            jAddress.setPostCode(jobPostcode.getText().toString().trim());
            jAddress.setStreet(jobStreet.getText().toString().trim());
            String jDescription = jobDescription.getText().toString().trim();

        if(!TextUtils.isEmpty(jNumber)&&
        !TextUtils.isEmpty(jAddress.getName())&&
        !TextUtils.isEmpty(jAddress.getPostCode())&&
        !TextUtils.isEmpty(jAddress.getStreet())&&
        !TextUtils.isEmpty(jDescription)&&
            nManager.getName() != null) {
            removeOldJob();
            createNewJobObject();
            checkIfNewJobAlreadyExistsAndSave();
        }
        else{
            Toast.makeText(EditJob.this, getString(R.string.empty_fields),
                    Toast.LENGTH_LONG).show();
        }
        }
        else{
            Address jAddress = new Address();
            jAddress.setName(jobClientName.getText().toString().trim());
            jAddress.setPostCode(jobPostcode.getText().toString().trim());
            jAddress.setStreet(jobStreet.getText().toString().trim());
            String jDescription = jobDescription.getText().toString().trim();

            if(!TextUtils.isEmpty(jAddress.getName())&&
                    !TextUtils.isEmpty(jAddress.getPostCode())&&
                    !TextUtils.isEmpty(jAddress.getStreet())&&
                    !TextUtils.isEmpty(jDescription)) {
                removeOldJob();
                createNewJobObject();
                checkIfNewJobAlreadyExistsAndSave();
            }
            else{
                Toast.makeText(EditJob.this, getString(R.string.empty_fields),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onRadioButtonEditJobClicked(View view) {
        switch (view.getId()) {
            case R.id.instRadioButtonEditJob:{
                jNumberLinearLayout.setVisibility(View.VISIBLE);
                managerLinearLayout.setVisibility(View.VISIBLE);
                jobType=JobType.INSTALLATION;
                managerList.clear();
                managerList.add(null);
                databaseReferenceManager.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ps : dataSnapshot.getChildren()) {
                            Manager manager = ps.getValue(Manager.class);
                            managerList.add(manager);
                        }
                        managerSpinnerAdapter = new ManagerSpinnerAdapter(EditJob.this, managerList);
                        jobManager.setAdapter(managerSpinnerAdapter);
                        int position = -1;
                        for (int i = 1; i < managerList.size(); i++) {
                            if (managerList.get(i).getName().equals(oManager.getName())
                                    &&managerList.get(i).getSurname().equals(oManager.getSurname())) {
                                position = i;
                            }
                        }
                        jobManager.setSelection(position);

                        jobManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Manager clickedItem = (Manager) parent.getItemAtPosition(position);
                                if(clickedItem!=null){
                                nManager.setName(clickedItem.getName());
                                nManager.setSurname(clickedItem.getSurname());
                                nManager.setEmailAddress(clickedItem.getEmailAddress());
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
                break;
            }
            case R.id.maintRadioButtonEditJob:{
                jNumberLinearLayout.setVisibility(View.GONE);
                managerLinearLayout.setVisibility(View.GONE);
                jobType=JobType.MAINTENANCE;
                break;
            }
            case R.id.servRadioButtonEditJob:{
                jNumberLinearLayout.setVisibility(View.GONE);
                managerLinearLayout.setVisibility(View.GONE);
                jobType=JobType.SERVICE;
                break;
            }
            case R.id.callRadioButtonEditJob:{
                jNumberLinearLayout.setVisibility(View.GONE);
                managerLinearLayout.setVisibility(View.GONE);
                jobType=JobType.CALL_OUT;
                break;
            }
        }
    }
}
