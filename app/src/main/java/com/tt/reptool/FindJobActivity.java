package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tt.reptool.adapters.ManagerSpinnerAdapter;
import com.tt.reptool.adapters.RecyclerViewAdapterJob;
import com.tt.reptool.javaClasses.Job;
import com.tt.reptool.javaClasses.JobType;
import com.tt.reptool.javaClasses.Manager;

import java.util.ArrayList;
import java.util.List;

public class FindJobActivity extends AppCompatActivity {

    private EditText jobNumberEditText, jobPostCodeEditText, jobNameEditText;
    private Spinner projectManagerSpinner;
    private List<Manager> mList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceManager;
    private DatabaseReference databaseReferenceJob;
    private DatabaseReference databaseReferenceJobMaintenance;
    private DatabaseReference databaseReferenceJobService;
    private DatabaseReference databaseReferenceJobCallOut;
    private ManagerSpinnerAdapter managerSpinnerAdapter;
    private Manager pm = new Manager();
    private List<Job> jList = new ArrayList<>();
    private List<Job> jobTempList = new ArrayList<>();
    private List<Job> jobTList = new ArrayList<>();
    private String jobNumber, postCode, name, projectManagerEmail;
    private LinearLayout jobNumberLinearLayout, postcodeLinearLayout, nameLinearLayout, projectManagerLinearLayout;
    private JobType jType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_job);
        jobNumberEditText = findViewById(R.id.jobNumberFindJobActivity);
        jobPostCodeEditText = findViewById(R.id.jobPostcodeFindJobActivity);
        jobNameEditText = findViewById(R.id.jobNameFindJobActivity);
        projectManagerSpinner = findViewById(R.id.jobPMFindJobActivity);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceManager = firebaseDatabase.getReference(getString(R.string.firebasepath_manager));
        databaseReferenceJob = firebaseDatabase.getReference(getString(R.string.firebasepath_job));
        databaseReferenceJobMaintenance = firebaseDatabase.getReference(getString(R.string.firebasepath_job_maintenance));
        databaseReferenceJobService = firebaseDatabase.getReference(getString(R.string.firebasepath_job_service));
        databaseReferenceJobCallOut = firebaseDatabase.getReference(getString(R.string.firebasepath_job_callout));
        jobNumberLinearLayout = findViewById(R.id.jobNumberLinearLayoutFindJob);
        postcodeLinearLayout = findViewById(R.id.postcodeLinearLayoutFindJob);
        nameLinearLayout = findViewById(R.id.nameLinearLayoutFindJob);
        projectManagerLinearLayout = findViewById(R.id.projectManagerLinearLayoutFindJob);
        initJobList();
        jobNumberLinearLayout.setVisibility(View.GONE);
        postcodeLinearLayout.setVisibility(View.GONE);
        nameLinearLayout.setVisibility(View.GONE);
        projectManagerLinearLayout.setVisibility(View.GONE);
        mList.clear();
        mList.add(null);
        databaseReferenceManager.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps: dataSnapshot.getChildren()){
                    Manager manager = ps.getValue(Manager.class);
                    mList.add(manager);
                }
                managerSpinnerAdapter = new ManagerSpinnerAdapter(FindJobActivity.this,mList);
                projectManagerSpinner.setAdapter(managerSpinnerAdapter);
                projectManagerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Manager clickedItem = (Manager)parent.getItemAtPosition(position);
                        if(clickedItem!=null){
                            pm.setName(clickedItem.getName());
                            pm.setSurname(clickedItem.getSurname());
                            pm.setEmailAddress(clickedItem.getEmailAddress());
                            projectManagerEmail = clickedItem.getEmailAddress();

                        }else{
                            projectManagerEmail = "";
                        }
                        checkJobsWithCriteria();
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
        jobNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkJobsWithCriteria();
            }
        });
        jobPostCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkJobsWithCriteria();
            }
        });
        jobNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkJobsWithCriteria();
            }
        });



    }

    private void checkJobsWithCriteria() {

        if(jType==JobType.INSTALLATION){
        jobTempList.clear();
        jobNumber = jobNumberEditText.getText().toString().trim();
        if(!jobNumber.isEmpty()){
            jobNumber = jobNumber.toUpperCase();
        }
        postCode = jobPostCodeEditText.getText().toString().trim();
        if(!postCode.isEmpty()){
            postCode = postCode.toUpperCase();
        }
        name = jobNameEditText.getText().toString().trim();

        for(int i=0;i<jobTList.size();i++){
            if(jobTList.get(i).getJobNumber().contains(jobNumber)&&
                jobTList.get(i).getAddress().getPostCode().contains(postCode)&&
                jobTList.get(i).getAddress().getName().contains(name)
                    && jList.get(i).getProjectManager().getEmailAddress().contains(projectManagerEmail)){
                jobTempList.add(jobTList.get(i));
            }
        }
        initRecycleView(jobTempList);
    }

    else{
            jobTempList.clear();
            postCode = jobPostCodeEditText.getText().toString().trim();
            if(!postCode.isEmpty()){
                postCode = postCode.toUpperCase();
            }
            name = jobNameEditText.getText().toString().trim();

            for(int i=0;i<jobTList.size();i++){
                if(jobTList.get(i).getAddress().getPostCode().contains(postCode)&&
                        jobTList.get(i).getAddress().getName().contains(name)){
                    jobTempList.add(jobTList.get(i));
                }
            }
            initRecycleView(jobTempList);
    }
    }

    private void initJobList() {
        jList.clear();
        databaseReferenceJob.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){
                    Job job = ps.getValue(Job.class);
                    jList.add(job);
                }
                databaseReferenceJobMaintenance.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshotMaintenance) {
                        for(DataSnapshot ps : dataSnapshotMaintenance.getChildren()){
                            Job job = ps.getValue(Job.class);
                            jList.add(job);
                        }

                        databaseReferenceJobService.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshotService) {
                                for (DataSnapshot ps : dataSnapshotService.getChildren()){
                                    Job job = ps.getValue(Job.class);
                                    jList.add(job);
                                }
                                databaseReferenceJobCallOut.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshotCallOut) {
                                        for (DataSnapshot ps : dataSnapshotCallOut.getChildren()){
                                            Job job = ps.getValue(Job.class);
                                            jList.add(job);
                                        }
                                        initRecycleView(jList);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRecycleView(final List<Job> list) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewFindJobActivity);
        RecyclerViewAdapterJob adapter = new RecyclerViewAdapterJob(this,list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new RecyclerViewAdapterJob.OnItemClickListener() {
            @Override
            public void onDeleteClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FindJobActivity.this);
                builder.setCancelable(true);
                builder.setTitle(R.string.deleting);
                builder.setMessage(getString(R.string.are_you_sure_you_want_delete)+"\n"+
                        list.get(position).getJobNumber()+" "+
                        list.get(position).getAddress().getName()+"?");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeJob(list, position);
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
            @Override
            public void onEditClick(int position) {
                editJob(list, position);
            }
        });
    }

    private void editJob(List<Job> list, int position) {
        JobType jobType = list.get(position).getJobType();
        if(jobType==null||jobType==JobType.INSTALLATION) {
            String jobNumber = list.get(position).getJobNumber();
            Intent intent = new Intent(this, EditJob.class);
            intent.putExtra(getString(R.string.extra_jobNumber), jobNumber);
            intent.putExtra("jobType",JobType.INSTALLATION.toString());
            startActivity(intent);
            finish();
        }
        else{
            String jobName = list.get(position).getAddress().getName();
            Intent intent = new Intent(this, EditJob.class);
            intent.putExtra("jobName", jobName);
            intent.putExtra("jobType",jobType.toString());
            startActivity(intent);
            finish();
        }
    }

    private void removeJob(List<Job> list, int position) {

        if(list.get(position).getJobType()!=null) {
            JobType jt = list.get(position).getJobType();

            switch (jt) {

                case MAINTENANCE: {
                    databaseReferenceJobMaintenance.child(list.get(position).getAddress().getName()).removeValue();
                    Intent intent = new Intent(this, FindJobActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case SERVICE: {
                    databaseReferenceJobService.child(list.get(position).getAddress().getName()).removeValue();
                    Intent intent = new Intent(this, FindJobActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case CALL_OUT: {
                    databaseReferenceJobCallOut.child(list.get(position).getAddress().getName()).removeValue();
                    Intent intent = new Intent(this, FindJobActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case INSTALLATION: {
                    databaseReferenceJob.child(list.get(position).getJobNumber()).removeValue();
                    Intent intent = new Intent(this, FindJobActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
            }
        }
        else{
            databaseReferenceJob.child(list.get(position).getJobNumber()).removeValue();
            Intent intent = new Intent(this, FindJobActivity.class);
            startActivity(intent);
            finish();

        }

    }

    private void initlist(JobType jType) {

    }


    public void onRadioButtonFindJobClicked(View view) {
        boolean checked = ((RadioButton)view).isChecked();



        switch (view.getId()){
            case R.id.instRadioButtonFindJob:{
                if(checked){
                jType=JobType.INSTALLATION;
                createList(jType);
                jobNumberLinearLayout.setVisibility(View.VISIBLE);
                postcodeLinearLayout.setVisibility(View.VISIBLE);
                nameLinearLayout.setVisibility(View.VISIBLE);
                projectManagerLinearLayout.setVisibility(View.VISIBLE);
                }
                break;
            }
            case R.id.maintRadioButtonFindJob:{
                if(checked){
                jType=JobType.MAINTENANCE;
                createList(jType);
                    jobNumberLinearLayout.setVisibility(View.GONE);
                    postcodeLinearLayout.setVisibility(View.VISIBLE);
                    nameLinearLayout.setVisibility(View.VISIBLE);
                    projectManagerLinearLayout.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.servRadioButtonFindJob:{
                if(checked){
                jType=JobType.SERVICE;
                createList(jType);
                    jobNumberLinearLayout.setVisibility(View.GONE);
                    postcodeLinearLayout.setVisibility(View.VISIBLE);
                    nameLinearLayout.setVisibility(View.VISIBLE);
                    projectManagerLinearLayout.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.callRadioButtonFindJob:{
                if(checked){
                jType=JobType.CALL_OUT;
                createList(jType);
                    jobNumberLinearLayout.setVisibility(View.GONE);
                    postcodeLinearLayout.setVisibility(View.VISIBLE);
                    nameLinearLayout.setVisibility(View.VISIBLE);
                    projectManagerLinearLayout.setVisibility(View.GONE);
                }
                break;
            }

        }

    }

    private void createList(JobType jType) {
        jobTList.clear();

        if(jType==JobType.INSTALLATION){

        for(int i = 0; i<jList.size();i++){
            if(jList.get(i).getJobType()==jType||jList.get(i).getJobType()==null){
                jobTList.add(jList.get(i));
            }
        }
        }
        else{
            for(int i = 0; i<jList.size();i++){
                if(jList.get(i).getJobType()==jType){
                    jobTList.add(jList.get(i));
                }
            }
        }
        initRecycleView(jobTList);
    }
}
