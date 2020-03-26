package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tt.reptool.javaClasses.DailyReport;
import com.tt.reptool.javaClasses.DateAndTime;
import com.tt.reptool.javaClasses.Type;

public class EditDailyReport extends AppCompatActivity {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceWeeklyReport;
    private DatabaseReference databaseReferenceAllReports;
    private DailyReport dailyReport = new DailyReport();
    private Spinner typeSpinner, jobNumberSpinner;
    private TextView dayTextView, startTimeTextView, endTimeTextView, jobOverviewTextView;
    private LinearLayout jobSpinnerLinearLayout, jobOverviewLinearLayout, descriptionLinearLayout, jobInfoLinearLayout, accidentsLinearLayout;
    private EditText jobDescription, jobInfo, jobAccidents;
    private Type type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daily_report);
        Bundle extras = getIntent().getExtras();
        String date = extras.getString("Date");

        typeSpinner = findViewById(R.id.spinnerDayTypeEditReport);
        dayTextView = findViewById(R.id.startDateEditReport);
        startTimeTextView = findViewById(R.id.startTimeEditReport);
        endTimeTextView = findViewById(R.id.endTimeEditReport);
        jobSpinnerLinearLayout = findViewById(R.id.jobSpinnerLinearLayoutEditReport);
        jobNumberSpinner = findViewById(R.id.jobNumberSpinnerEditReport);
        jobOverviewLinearLayout = findViewById(R.id.jobOverviewLinearLayoutEditReport);
        jobOverviewTextView = findViewById(R.id.jobOverviewEditReport);
        descriptionLinearLayout = findViewById(R.id.descriptionLinearLayoutEditReport);
        jobDescription = findViewById(R.id.jobDescriptionActivityEditReport);
        jobInfoLinearLayout = findViewById(R.id.jobInfoLinearLayoutEditReport);
        jobInfo = findViewById(R.id.jobInfoActivityEditReport);
        accidentsLinearLayout = findViewById(R.id.accidentsLinearLayoutEditReport);
        jobAccidents = findViewById(R.id.jobAccidentsActivityEditReport);

        typeSpinner.setAdapter(new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_item, Type.values()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceAllReports = firebaseDatabase.getReference(getString(R.string.firebasepath_all_reports));
        databaseReferenceWeeklyReport = firebaseDatabase.getReference(getString(R.string.firebasepath_weekly_reports));
        databaseReferenceWeeklyReport.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    dailyReport = dataSnapshot.getValue(DailyReport.class);
                }
                type=dailyReport.getWorkReport().getType();

                if(type==Type.WORK){
                    typeSpinner.setSelection(0);
                    workLayout();
                }else if(type==Type.DAY_OFF){
                    typeSpinner.setSelection(1);
                    dayOffLayout();
                }else if(type==Type.BANK_HOLIDAY){
                    typeSpinner.setSelection(2);
                    bankHolidayLayout();
                }else if(type==Type.TRAINING){
                    typeSpinner.setSelection(3);
                    trainingLayout();
                }else;
                dayTextView.setText(showDate(dailyReport));
                startTimeTextView.setText(showTime(dailyReport.getStartTime()));
                endTimeTextView.setText(showTime(dailyReport.getEndTime()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = (Type)parent.getItemAtPosition(position);
                if(type==Type.WORK){
                    workLayout();
                }else if(type==Type.DAY_OFF){
                    dayOffLayout();
                }else if(type==Type.BANK_HOLIDAY){
                    bankHolidayLayout();
                }else if(type==Type.TRAINING){
                    trainingLayout();
                }else;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private String showTime(DateAndTime startTime) {
        return startTime.getHour()+":"+startTime.getMinute();
    }

    private String showDate(DailyReport dailyReport) {
        return dailyReport.getStartTime().getDay()+"/"+
                dailyReport.getStartTime().getMonth()+"/"+
                dailyReport.getStartTime().getYear();
    }

    private void trainingLayout() {
        jobSpinnerLinearLayout.setVisibility(View.GONE);
        jobOverviewLinearLayout.setVisibility(View.GONE);
        descriptionLinearLayout.setVisibility(View.VISIBLE);
        jobInfoLinearLayout.setVisibility(View.GONE);
        accidentsLinearLayout.setVisibility(View.GONE);
    }

    private void bankHolidayLayout() {
        jobSpinnerLinearLayout.setVisibility(View.GONE);
        jobOverviewLinearLayout.setVisibility(View.GONE);
        descriptionLinearLayout.setVisibility(View.GONE);
        jobInfoLinearLayout.setVisibility(View.GONE);
        accidentsLinearLayout.setVisibility(View.GONE);
    }

    private void dayOffLayout() {
        jobSpinnerLinearLayout.setVisibility(View.GONE);
        jobOverviewLinearLayout.setVisibility(View.GONE);
        descriptionLinearLayout.setVisibility(View.GONE);
        jobInfoLinearLayout.setVisibility(View.GONE);
        accidentsLinearLayout.setVisibility(View.GONE);
    }

    private void workLayout() {
        jobSpinnerLinearLayout.setVisibility(View.VISIBLE);
        jobOverviewLinearLayout.setVisibility(View.VISIBLE);
        descriptionLinearLayout.setVisibility(View.VISIBLE);
        jobInfoLinearLayout.setVisibility(View.VISIBLE);
        accidentsLinearLayout.setVisibility(View.VISIBLE);
    }


    public void onClickCancelEditing(View view) {
        Intent intent = new Intent(this,WeeklyReportsActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickSaveEditing(View view) {
    }
}
