package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tt.reptool.singletons.JobSingleton;
import com.tt.reptool.adapters.RecyclerViewAdapterShortJob;
import com.tt.reptool.javaClasses.Job;
import com.tt.reptool.javaClasses.JobType;

import java.util.ArrayList;
import java.util.List;

public class JobsToChoose extends AppCompatActivity {

    private List<Job> jobList = new ArrayList<>();
    private List<Job> jobTempList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceJob;
    private DatabaseReference databaseReferenceJobMaintenance;
    private DatabaseReference databaseReferenceJobService;
    private DatabaseReference databaseReferenceJobCallOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_to_choose);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceJob = firebaseDatabase.getReference(getString(R.string.firebasepath_job));
        databaseReferenceJobMaintenance = firebaseDatabase.getReference(getString(R.string.firebasepath_job_maintenance));
        databaseReferenceJobService = firebaseDatabase.getReference(getString(R.string.firebasepath_job_service));
        databaseReferenceJobCallOut = firebaseDatabase.getReference(getString(R.string.firebasepath_job_callout));
        jobList.clear();
        databaseReferenceJob.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){
                    Job job = ps.getValue(Job.class);
                    jobList.add(job);
                }
                databaseReferenceJobMaintenance.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshotMaintenance) {
                        for(DataSnapshot ps : dataSnapshotMaintenance.getChildren()){
                            Job job = ps.getValue(Job.class);
                            jobList.add(job);
                        }

                        databaseReferenceJobService.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshotService) {
                                for (DataSnapshot ps : dataSnapshotService.getChildren()){
                                    Job job = ps.getValue(Job.class);
                                    jobList.add(job);
                                }
                                databaseReferenceJobCallOut.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshotCallOut) {
                                        for (DataSnapshot ps : dataSnapshotCallOut.getChildren()){
                                            Job job = ps.getValue(Job.class);
                                            jobList.add(job);
                                        }
                                        initRecycleView(jobList);
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

    private void initRecycleView(final List<Job> jList) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewJobToChoose);
        RecyclerViewAdapterShortJob adapter = new RecyclerViewAdapterShortJob(this,jList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnSelectedItemClickListener(new RecyclerViewAdapterShortJob.OnItemClickListener() {
            @Override
            public void onSelectedItemClick(int position) {
                Job job = jList.get(position);
                JobSingleton.getInstance().saveJobToSingleton(job);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("KEY_GOES_HERE", true);
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });
    }


    public void onRadioButtonJobToChooseClicked(View view) {
        boolean checked = ((RadioButton)view).isChecked();
        jobTempList.clear();
        switch (view.getId()){
            case R.id.instRadioButtonJobToChoose:{
                if(checked) {
                    for (int i = 0; i < jobList.size(); i++) {
                        if ((jobList.get(i).getJobType() == JobType.INSTALLATION&&!jobList.get(i).isFinished()) || jobList.get(i).getJobType() == null) {
                            jobTempList.add(jobList.get(i));
                        }
                    }

                    initRecycleView(jobTempList);
                    break;
                }
            }
            case R.id.maintRadioButtonJobToChoose:{
                if(checked) {
                    for (int i = 0; i < jobList.size(); i++) {
                        if (jobList.get(i).getJobType() == JobType.MAINTENANCE) {
                            jobTempList.add(jobList.get(i));
                        }
                    }
                    initRecycleView(jobTempList);
                    break;
                }
            }
            case R.id.servRadioButtonJobToChoose:{
                if(checked) {
                    for (int i = 0; i < jobList.size(); i++) {
                        if (jobList.get(i).getJobType() == JobType.SERVICE||(jobList.get(i).getJobType() == JobType.INSTALLATION&&jobList.get(i).isFinished())) {
                            jobTempList.add(jobList.get(i));
                        }
                    }
                    initRecycleView(jobTempList);
                    break;
                }
            }
            case R.id.callRadioButtonJobToChoose:{
                if(checked) {
                    for (int i = 0; i < jobList.size(); i++) {
                        if (jobList.get(i).getJobType() == JobType.CALL_OUT) {
                            jobTempList.add(jobList.get(i));
                        }
                    }
                    initRecycleView(jobTempList);
                    break;
                }
            }
        }
    }
}