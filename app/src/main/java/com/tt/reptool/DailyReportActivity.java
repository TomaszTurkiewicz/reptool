package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DailyReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener


{
        private static final int START_TIME = 0;
        private static final int END_TIME = 1;
        private TextView startDate, startTime, endTime, jobOverview;
        private EditText jobDescription, info, accidents;
        private Spinner jobNumberSpinner;
        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference databaseReferenceJob;
        private DatabaseReference databaseReferenceWeeklyReports;
        private DatabaseReference databaseReferenceAllReports;
        private Calendar calendar, calendarEnd;
        private int flag;
        private List<Job> jobList = new ArrayList<>();
        private JobSpinnerAdapter jobSpinnerAdapter;
        private Job job = new Job();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);
        startDate = findViewById(R.id.startDate);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        jobOverview = findViewById(R.id.jobOverview);
        jobDescription = findViewById(R.id.jobDescriptionActivityDailyReport);
        info = findViewById(R.id.jobInfoActivityDailyReport);
        accidents = findViewById(R.id.jobAccidentsActivityDailyReport);
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
        return (c.getYear())+"_"+
                (c.getMonth())+"_"+
                (c.getDay());
    }

    public String showTime(Calendar c){
        return c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
    }

    public void saveDailyReport(View view) {
        String desc = jobDescription.getText().toString().trim();
        String jInfo = info.getText().toString().trim();
        String acc = accidents.getText().toString().trim();

        DateAndTime startTime = new DateAndTime();
        startTime.setDateAndTime(calendar);

        DateAndTime endTime = new DateAndTime();
        endTime.setDateAndTime(calendarEnd);

        DailyReport dailyReport = new DailyReport(startTime,endTime,job,desc,jInfo,acc);
        databaseReferenceWeeklyReports=firebaseDatabase.getReference(getString(R.string.firebasepath_weekly_reports));
        databaseReferenceWeeklyReports
                .child(showDateBackwards(dailyReport.getStartTime()))
                .setValue(dailyReport);

        databaseReferenceAllReports=firebaseDatabase.getReference(getString(R.string.firebasepath_all_reports));
        databaseReferenceAllReports
                .child(showDateBackwards(dailyReport.getStartTime()))
                .setValue(dailyReport);


    }
}

// TODO change day of week from int to string (names)
// TODO empty fields
// TODO check if already exists
//TODO change picking up job (filtering), maybe different activity?