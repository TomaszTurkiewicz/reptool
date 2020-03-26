package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tt.reptool.javaClasses.DailyReport;

public class EditDailyReport extends AppCompatActivity {

    private TextView textView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceWeeklyReport;
    private DatabaseReference databaseReferenceAllReports;
    private DailyReport dailyReport = new DailyReport();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daily_report);
        Bundle extras = getIntent().getExtras();
        String date = extras.getString("Date");
        textView = findViewById(R.id.tv);
        textView.setText(date);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceAllReports = firebaseDatabase.getReference(getString(R.string.firebasepath_all_reports));
        databaseReferenceWeeklyReport = firebaseDatabase.getReference(getString(R.string.firebasepath_weekly_reports));
        databaseReferenceWeeklyReport.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    dailyReport = dataSnapshot.getValue(DailyReport.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void onClickCancelEditing(View view) {
        Intent intent = new Intent(this,WeeklyReportsActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickSaveEditing(View view) {
    }
}
