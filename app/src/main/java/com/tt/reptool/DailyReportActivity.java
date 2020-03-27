package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import com.tt.reptool.fragments.DatePickerFragment;
import com.tt.reptool.fragments.TimeStartPickerFragment;
import com.tt.reptool.javaClasses.Address;
import com.tt.reptool.javaClasses.DailyReport;
import com.tt.reptool.javaClasses.DateAndTime;
import com.tt.reptool.javaClasses.Job;
import com.tt.reptool.javaClasses.Manager;
import com.tt.reptool.javaClasses.Type;
import com.tt.reptool.javaClasses.WorkReport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener


{
        private static final int START_TIME = 0;
        private static final int END_TIME = 1;
        private TextView startDate, startTime, endTime, jobOverview;
        private EditText jobDescription, info, accidents;
        private Spinner jobNumberSpinner, dayTypeSpinner;
        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference databaseReferenceJob;
        private DatabaseReference databaseReferenceWeeklyReports;
        private DatabaseReference databaseReferenceAllReports;
        private Calendar calendar, calendarEnd;
        private int flag;
        private List<Job> jobList = new ArrayList<>();
        private JobSpinnerAdapter jobSpinnerAdapter;
        private Job job = new Job();
        private Type type;
        private LinearLayout jobSpinnerLinearLayout, jobOverviewLinearLayout, descriptionLinearLayout,
                jobInfoLinearLayout, accidentsLinearLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        getWindow().getDecorView().setSystemUiVisibility(flags);
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){

            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if((visibility&View.SYSTEM_UI_FLAG_FULLSCREEN)==0){
                    decorView.setSystemUiVisibility(flags);
                }
            }
        });


        startDate = findViewById(R.id.startDate);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        jobOverview = findViewById(R.id.jobOverview);
        jobDescription = findViewById(R.id.jobDescriptionActivityDailyReport);
        info = findViewById(R.id.jobInfoActivityDailyReport);
        accidents = findViewById(R.id.jobAccidentsActivityDailyReport);
        dayTypeSpinner = findViewById(R.id.spinnerDayType);
        jobSpinnerLinearLayout = findViewById(R.id.jobSpinnerLinearLayout);
        jobOverviewLinearLayout = findViewById(R.id.jobOverviewLinearLayout);
        descriptionLinearLayout = findViewById(R.id.descriptionLinearLayout);
        jobInfoLinearLayout = findViewById(R.id.jobInfoLinearLayout);
        accidentsLinearLayout = findViewById(R.id.accidentsLinearLayout);
        dayTypeSpinner.setAdapter(new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_item, Type.values()));
        dayTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = (Type)parent.getItemAtPosition(position);
                if (type==Type.WORK){
                    setWorkReportLayout();
                }
                else if(type == Type.DAY_OFF){
                    setDayOffReportLayout();
                }
                else if(type == Type.TRAINING){
                    setTrainingReportLayout();
                }
                else if(type == Type.BANK_HOLIDAY){
                    setBankHolidayReportLayout();
                }
                else;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        calendar = Calendar.getInstance();
        calendarEnd = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendarEnd.set(Calendar.HOUR_OF_DAY,17);
        calendar.set(Calendar.MINUTE,30);
        calendarEnd.set(Calendar.MINUTE,30);
        flag=0;
        startDate.setText(showDate(calendar));
        startTime.setText(showTime(calendar));
        endTime.setText(showTime(calendarEnd));
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceJob=firebaseDatabase.getReference(getString(R.string.firebasepath_job));
        jobList.clear();
        jobList.add(null);
        databaseReferenceJob.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){
                    Job jobTemp = ps.getValue(Job.class);
                    jobList.add(jobTemp);
                }
                jobNumberSpinner = (Spinner)findViewById(R.id.jobNumberSpinner);
                jobSpinnerAdapter = new JobSpinnerAdapter(DailyReportActivity.this,jobList);
                jobNumberSpinner.setAdapter(jobSpinnerAdapter);
                jobNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Job clickedItem = (Job)parent.getItemAtPosition(position);

                        if (clickedItem!=null){
                        job.setJobNumber(clickedItem.getJobNumber());
                            job.setAddress(new Address(clickedItem.getAddress().getName(),
                                    clickedItem.getAddress().getStreet(),
                                    clickedItem.getAddress().getPostCode()));
                            job.setShortDescription(clickedItem.getShortDescription());
                            job.setProjectManager(new Manager(clickedItem.getProjectManager().getName(),
                                    clickedItem.getProjectManager().getSurname(),
                                    clickedItem.getProjectManager().getEmailAddress()));
                        jobOverview.setText(job.getJobNumber()+" "
                                +job.getProjectManager().getName()+" "
                        +job.getProjectManager().getSurname()+" "
                        +job.getAddress().getName()+" "
                        +job.getAddress().getStreet()+" "
                        +job.getAddress().getPostCode());
                    }
                        else{
                            job.setJobNumber("");
                            job.setAddress(new Address("",
                                    "",
                                    ""));
                            job.setShortDescription("");
                            job.setProjectManager(new Manager("",
                                    "",
                                    ""));
                            jobOverview.setText(job.getJobNumber()+" "
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStartPickerFragment timeEndPicker = new TimeStartPickerFragment();
                timeEndPicker.setHour(calendarEnd.get(Calendar.HOUR_OF_DAY));
                timeEndPicker.setMin(calendarEnd.get(Calendar.MINUTE));
                flag=END_TIME;
                timeEndPicker.show(getSupportFragmentManager(),"time picker");
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStartPickerFragment timeStartPicker = new TimeStartPickerFragment();
                flag=START_TIME;
                timeStartPicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                timeStartPicker.setMin(calendar.get(Calendar.MINUTE));
                timeStartPicker.show(getSupportFragmentManager(),"time picker");

            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

       if(flag==START_TIME) {
           calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
           calendar.set(Calendar.MINUTE, minute);
           startTime.setText(showTime(calendar));
       }else if(flag==END_TIME){
           calendarEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
           calendarEnd.set(Calendar.MINUTE, minute);
           endTime.setText(showTime(calendarEnd));
        }
    }

    @Override
    public void onDateSet(DatePicker view, int yearPicker, int monthPicker, int dayOfMonthPicker) {

        calendar.set(Calendar.YEAR,yearPicker);
        calendar.set(Calendar.MONTH,monthPicker);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonthPicker);

        calendarEnd.set(Calendar.YEAR,yearPicker);
        calendarEnd.set(Calendar.MONTH,monthPicker);
        calendarEnd.set(Calendar.DAY_OF_MONTH,dayOfMonthPicker);

        startDate.setText(showDate(calendar));

    }

    public String showDate(Calendar c){
        return c.get(Calendar.DAY_OF_MONTH)+"/"+
                (c.get(Calendar.MONTH)+1)+"/"+
                c.get(Calendar.YEAR);
    }
    public String showDateBackwards (DateAndTime c){
        String format = "%1$02d";

        String month = String.format(format, c.getMonth());
        String day = String.format(format, c.getDay());

        return (c.getYear())+"_"+
                month + "_"+
                day;
    }

    public String showTime(Calendar c){
        return c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
    }

    public void saveDailyReport(View view) {

        DateAndTime startTime = new DateAndTime();
        startTime.setDateAndTime(calendar);

        DateAndTime endTime = new DateAndTime();
        endTime.setDateAndTime(calendarEnd);
        String jInfo = info.getText().toString().trim();
        String acc = accidents.getText().toString().trim();
        String desc = jobDescription.getText().toString().trim();

        if(type==Type.BANK_HOLIDAY){
            storeData(startTime,endTime,desc,jInfo,acc);
        }
        else if(type==Type.DAY_OFF){
            storeData(startTime,endTime,desc,jInfo,acc);
        }
        else if(type==Type.TRAINING){
                if(!TextUtils.isEmpty(desc)) {
                    desc = jobDescription.getText().toString().trim();
                    storeData(startTime,endTime,desc,jInfo,acc);
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
        }
        else{
            if(!TextUtils.isEmpty(desc)&&!job.getJobNumber().isEmpty()){
                desc = jobDescription.getText().toString().trim();
                storeData(startTime,endTime,desc,jInfo,acc);
            }else{
                Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
            }
        }



    }
    public void storeData(DateAndTime startTime, DateAndTime endTime, String desc, String jInfo, String acc){
        final DailyReport dailyReport = new DailyReport(startTime,endTime,new WorkReport(type, job, desc, jInfo, acc));

        databaseReferenceWeeklyReports=firebaseDatabase.getReference(getString(R.string.firebasepath_weekly_reports));
        databaseReferenceAllReports=firebaseDatabase.getReference(getString(R.string.firebasepath_all_reports));

        databaseReferenceAllReports.child(showDateBackwards(dailyReport.getStartTime()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(DailyReportActivity.this,getString(R.string.report_exists),Toast.LENGTH_LONG).show();
                        }
                        else{
                            databaseReferenceWeeklyReports
                                    .child(showDateBackwards(dailyReport.getStartTime()))
                                    .setValue(dailyReport);

                            databaseReferenceAllReports
                                    .child(showDateBackwards(dailyReport.getStartTime()))
                                    .setValue(dailyReport);
                            Toast.makeText(DailyReportActivity.this,R.string.daily_report_saved,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(DailyReportActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }

    public void setWorkReportLayout(){
        jobSpinnerLinearLayout.setVisibility(View.VISIBLE);
        jobOverviewLinearLayout.setVisibility(View.VISIBLE);
        descriptionLinearLayout.setVisibility(View.VISIBLE);
        jobInfoLinearLayout.setVisibility(View.VISIBLE);
        accidentsLinearLayout.setVisibility(View.VISIBLE);
    }
    public void setDayOffReportLayout(){
        jobSpinnerLinearLayout.setVisibility(View.GONE);
        jobOverviewLinearLayout.setVisibility(View.GONE);
        descriptionLinearLayout.setVisibility(View.GONE);
        jobInfoLinearLayout.setVisibility(View.GONE);
        accidentsLinearLayout.setVisibility(View.GONE);
    }
    public void setBankHolidayReportLayout(){
        jobSpinnerLinearLayout.setVisibility(View.GONE);
        jobOverviewLinearLayout.setVisibility(View.GONE);
        descriptionLinearLayout.setVisibility(View.GONE);
        jobInfoLinearLayout.setVisibility(View.GONE);
        accidentsLinearLayout.setVisibility(View.GONE);
    }
    public void setTrainingReportLayout(){
        jobSpinnerLinearLayout.setVisibility(View.GONE);
        jobOverviewLinearLayout.setVisibility(View.GONE);
        descriptionLinearLayout.setVisibility(View.VISIBLE);
        jobInfoLinearLayout.setVisibility(View.GONE);
        accidentsLinearLayout.setVisibility(View.GONE);
    }

    public void backToMainMenuOnClick(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}


// TODO change picking up job (filtering), maybe different activity?