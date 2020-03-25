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
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tt.reptool.adapters.ManagerSpinnerAdapter;
import com.tt.reptool.adapters.RecyclerViewAdapterJob;
import com.tt.reptool.javaClasses.Job;
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
    private ManagerSpinnerAdapter managerSpinnerAdapter;
    private Manager pm = new Manager();
    private List<Job> jList = new ArrayList<>();
    private List<Job> jobTempList = new ArrayList<>();
    private String jobNumber, postCode, name, projectManagerEmail;

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
        initJobList();
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
                            checkJobsWithCriteria();
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

        for(int i=0;i<jList.size();i++){
            if(jList.get(i).getJobNumber().contains(jobNumber)&&
                jList.get(i).getAddress().getPostCode().contains(postCode)&&
                jList.get(i).getAddress().getName().contains(name)&&
                jList.get(i).getProjectManager().getEmailAddress().contains(projectManagerEmail)){
                jobTempList.add(jList.get(i));
            }
        }
        initRecycleView(jobTempList);
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
                initRecycleView(jList);
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
        String jobNumber = list.get(position).getJobNumber();
        Intent intent = new Intent(this,EditJob.class);
        intent.putExtra(getString(R.string.extra_jobNumber),jobNumber);
        startActivity(intent);
        finish();
    }

    private void removeJob(List<Job> list, int position) {
        databaseReferenceJob.child(list.get(position).getJobNumber()).removeValue();
        initJobList();
    }

    public void showAllOnClick(View view) {
        Intent intent = new Intent(this, AllJobs.class);
        startActivity(intent);
    }

}
