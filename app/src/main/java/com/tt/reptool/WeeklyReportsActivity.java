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

public class WeeklyReportsActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceWeeklyReport;
    private DatabaseReference databaseReferenceAllReport;
    private List<DailyReport> wRepList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_reports);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceWeeklyReport = firebaseDatabase.getReference(getString(R.string.firebasepath_weekly_reports));
        databaseReferenceAllReport = firebaseDatabase.getReference(getString(R.string.firebasepath_all_reports));
        initReportList();
    }

    private void initReportList() {
        wRepList.clear();
        databaseReferenceWeeklyReport.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){

                    DailyReport dr = ps.getValue(DailyReport.class);
                    wRepList.add(dr);
                }
                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewWeeklyReportsActivity);
        RecyclerViewAdapterWeeklyReports adapter = new RecyclerViewAdapterWeeklyReports(this,wRepList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new RecyclerViewAdapterWeeklyReports.OnItemClickListener() {
            @Override
            public void onDeleteWeeklyReportClick(int position) {
                removeWeeklyReport(position);
            }

            @Override
            public void onEditWeeklyReportClick(int position) {

            }
        });
    }

    private void removeWeeklyReport(int position) {
        DateAndTime tempDate = wRepList.get(position).getStartTime();
        databaseReferenceWeeklyReport.child(showDateBackwards(tempDate)).removeValue();
        databaseReferenceAllReport.child(showDateBackwards(tempDate)).removeValue();
        initReportList();
    }

    public String showDateBackwards (DateAndTime c){
        return (c.getYear())+"_"+
                (c.getMonth())+"_"+
                (c.getDay());
    }


}

// TODO finish this activity - recyclerview plus boolean DailyReport (to send)
// TODO sending email with reports!!!
