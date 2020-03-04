package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllReportsActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<DailyReport> rList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reports);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(getString(R.string.firebasepath_all_reports));
        initReportList();
    }

    private void initReportList() {
        rList.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){

                    DailyReport dr = ps.getValue(DailyReport.class);
                    rList.add(dr);
                }
                Collections.reverse(rList);
                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAllReportsActivity);
        RecyclerViewAdapterReport adapter = new RecyclerViewAdapterReport(this,rList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
