package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllJobs extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<Job> jList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_jobs);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(getString(R.string.firebasepath_job));
        initJobList();
    }

    private void initJobList() {

        jList.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){
                    Job job = ps.getValue(Job.class);
                    jList.add(job);
                }
                initRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAllJobsActivity);
        RecyclerViewAdapterJob adapter = new RecyclerViewAdapterJob(this,jList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new RecyclerViewAdapterJob.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeJob(position);
            }

            @Override
            public void onEditClick(int position) {
                editJob(position);
            }
        });
    }

    private void editJob(int position) {
        String jobNumber = jList.get(position).getJobNumber();
        Intent intent = new Intent(this,EditJob.class);
        intent.putExtra(getString(R.string.extra_jobNumber),jobNumber);
        startActivity(intent);
        finish();
    }

    private void removeJob(int position) {
        databaseReference.child(jList.get(position).getJobNumber()).removeValue();
        initJobList();
    }
}

// TODO firebase path from string resources
//TODO add more spaces, margins in recyclerView