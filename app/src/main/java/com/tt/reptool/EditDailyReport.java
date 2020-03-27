package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tt.reptool.adapters.JobSpinnerAdapter;
import com.tt.reptool.fragments.TimeStartPickerFragment;
import com.tt.reptool.javaClasses.Address;
import com.tt.reptool.javaClasses.DailyReport;
import com.tt.reptool.javaClasses.DateAndTime;
import com.tt.reptool.javaClasses.Job;
import com.tt.reptool.javaClasses.Manager;
import com.tt.reptool.javaClasses.Type;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditDailyReport extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceWeeklyReport;
    private DatabaseReference databaseReferenceAllReports;
    private DatabaseReference databaseReferenceJob;
    private DailyReport dailyReport = new DailyReport();
    private Spinner typeSpinner, jobNumberSpinner;
    private TextView dayTextView, startTimeTextView, endTimeTextView, jobOverviewTextView;
    private LinearLayout jobSpinnerLinearLayout, jobOverviewLinearLayout, descriptionLinearLayout, jobInfoLinearLayout, accidentsLinearLayout;
    private EditText jobDescription, jobInfo, jobAccidents;
    private Type type;
    private JobSpinnerAdapter jobSpinnerAdapter;
    private List<Job> jobList = new ArrayList();
    private Job job = new Job();
    private static final int START_TIME = 0;
    private static final int END_TIME = 1;
    private int flag;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daily_report);
        Bundle extras = getIntent().getExtras();
        date = extras.getString("Date");

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
        databaseReferenceJob = firebaseDatabase.getReference(getString(R.string.firebasepath_job));
        jobList.clear();
        jobList.add(null);


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
                jobDescription.setText(dailyReport.getWorkReport().getDescription());
                jobInfo.setText(dailyReport.getWorkReport().getInfo());
                jobAccidents.setText(dailyReport.getWorkReport().getAccident());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReferenceJob.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ps : dataSnapshot.getChildren()) {
                    Job jobTemp = ps.getValue(Job.class);
                    jobList.add(jobTemp);
                }
                jobSpinnerAdapter = new JobSpinnerAdapter(EditDailyReport.this, jobList);
                jobNumberSpinner.setAdapter(jobSpinnerAdapter);

                if(type==Type.WORK){
                int position = -1;
                for (int i = 1; i < jobList.size(); i++) {
                    if (jobList.get(i).getJobNumber().equals(dailyReport.getWorkReport().getJob().getJobNumber())) {
                        position = i;
                    }
                }
                jobNumberSpinner.setSelection(position);
                jobOverviewTextView.setText(dailyReport.getWorkReport().getJob().getJobNumber() + " "
                        + dailyReport.getWorkReport().getJob().getProjectManager().getName() + " "
                        + dailyReport.getWorkReport().getJob().getProjectManager().getSurname() + " "
                        + dailyReport.getWorkReport().getJob().getAddress().getName() + " "
                        + dailyReport.getWorkReport().getJob().getAddress().getStreet() + " "
                        + dailyReport.getWorkReport().getJob().getAddress().getPostCode());
            }
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
                    setJobEmpty();
                }else if(type==Type.BANK_HOLIDAY){
                    bankHolidayLayout();
                    setJobEmpty();
                }else if(type==Type.TRAINING){
                    trainingLayout();
                    setJobEmpty();
                }else;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jobNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Job clickedItem = (Job)parent.getItemAtPosition(position);
                if(clickedItem!=null){
                job.setJobNumber(clickedItem.getJobNumber());
                job.setAddress(new Address(clickedItem.getAddress().getName(),
                        clickedItem.getAddress().getStreet(),
                        clickedItem.getAddress().getPostCode()));
                job.setShortDescription(clickedItem.getShortDescription());
                job.setProjectManager(new Manager(clickedItem.getProjectManager().getName(),
                        clickedItem.getProjectManager().getSurname(),
                        clickedItem.getProjectManager().getEmailAddress()));
                jobOverviewTextView.setText(job.getJobNumber()+" "
                        +job.getProjectManager().getName()+" "
                        +job.getProjectManager().getSurname()+" "
                        +job.getAddress().getName()+" "
                        +job.getAddress().getStreet()+" "
                        +job.getAddress().getPostCode());
            }else {
                    setJobEmpty();

                    jobOverviewTextView.setText(job.getJobNumber()+" "
                            +job.getProjectManager().getName()+" "
                            +job.getProjectManager().getSurname()+" "
                            +job.getAddress().getName()+" "
                            +job.getAddress().getStreet()+" "
                            +job.getAddress().getPostCode());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        startTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStartPickerFragment timeStartPicker = new TimeStartPickerFragment();
                flag=START_TIME;
                timeStartPicker.setHour(dailyReport.getStartTime().getHour());
                timeStartPicker.setMin(dailyReport.getStartTime().getMinute());
                timeStartPicker.show(getSupportFragmentManager(),"time picker");

            }
        });
        endTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStartPickerFragment timeStartPicker = new TimeStartPickerFragment();
                flag=END_TIME;
                timeStartPicker.setHour(dailyReport.getEndTime().getHour());
                timeStartPicker.setMin(dailyReport.getEndTime().getMinute());
                timeStartPicker.show(getSupportFragmentManager(),"time picker");

            }
        });

    }

    private void setJobEmpty() {
        job.setJobNumber("");
        job.setAddress(new Address("",
                "",
                ""));
        job.setShortDescription("");
        job.setProjectManager(new Manager("",
                "",
                ""));
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

        String desc = jobDescription.getText().toString().trim();
        String acc = jobAccidents.getText().toString().trim();
        String info = jobInfo.getText().toString().trim();
        dailyReport.getWorkReport().setType(type);

        if(type==Type.WORK) {
            if(!TextUtils.isEmpty(desc)&&!job.getJobNumber().isEmpty()) {
                dailyReport.getWorkReport().setJob(job);
                dailyReport.getWorkReport().setDescription(desc);
                dailyReport.getWorkReport().setAccident(acc);
                dailyReport.getWorkReport().setInfo(info);
                storeDailyReport();
            }else{
                Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
            }

        }else if(type==Type.TRAINING){
            if(!TextUtils.isEmpty(desc)) {
                dailyReport.getWorkReport().setJob(job);
                dailyReport.getWorkReport().setDescription(desc);
                dailyReport.getWorkReport().setAccident("");
                dailyReport.getWorkReport().setInfo("");
                storeDailyReport();
            }else{
                Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
            }
        }else{
            dailyReport.getWorkReport().setJob(job);
            dailyReport.getWorkReport().setDescription("");
            dailyReport.getWorkReport().setAccident("");
            dailyReport.getWorkReport().setInfo("");
            storeDailyReport();
        }

    }

    private void storeDailyReport() {
        databaseReferenceWeeklyReport.child(date).setValue(dailyReport);
        databaseReferenceAllReports.child(date).setValue(dailyReport);
        Intent intent = new Intent(EditDailyReport.this,WeeklyReportsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(flag==START_TIME){
        dailyReport.getStartTime().setHour(hourOfDay);
        dailyReport.getStartTime().setMinute(minute);
        startTimeTextView.setText(showTime(dailyReport.getStartTime()));
        }
        else if(flag==END_TIME){
            dailyReport.getEndTime().setHour(hourOfDay);
            dailyReport.getEndTime().setMinute(minute);
            endTimeTextView.setText(showTime(dailyReport.getEndTime()));
        }
    }
}
