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
import android.widget.Button;
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
import com.tt.reptool.javaClasses.JobType;
import com.tt.reptool.javaClasses.Manager;
import com.tt.reptool.javaClasses.Type;
import com.tt.reptool.javaClasses.WorkReport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

        private static final int START_TIME = 0;
        private static final int END_TIME = 1;
        private TextView startDate, startTime, endTime, jobOverview1;
        private EditText jobDescription1, info1, accidents1;
        private Spinner jobNumberSpinner1, dayTypeSpinner1;
        private Spinner jobNumberSpinner2, dayTypeSpinner2;
        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference databaseReferenceJob;
        private DatabaseReference databaseReferenceJobMaintenance;
        private DatabaseReference databaseReferenceJobService;
        private DatabaseReference databaseReferenceJobCallOut;
        private DatabaseReference databaseReferenceWeeklyReports;
        private DatabaseReference databaseReferenceAllReports;
        private Calendar calendar, calendarEnd;
        private int flag;
        private List<Job> jobList = new ArrayList<>();
        private JobSpinnerAdapter jobSpinnerAdapter;
        private Job job1 = new Job();
        private Type type1;
        private LinearLayout jobSpinnerLinearLayout1, jobOverviewLinearLayout1, descriptionLinearLayout1,
                jobInfoLinearLayout1, accidentsLinearLayout1;
        private LinearLayout workReport2, workReport3, workReport4, workReport5;
        private Button addWorkReport;
        private int workReportCounter;




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
        jobOverview1 = findViewById(R.id.jobOverview1);
        jobDescription1 = findViewById(R.id.jobDescriptionActivityDailyReport1);
        info1 = findViewById(R.id.jobInfoActivityDailyReport1);
        accidents1 = findViewById(R.id.jobAccidentsActivityDailyReport1);
        dayTypeSpinner1 = findViewById(R.id.spinnerDayType1);
        jobSpinnerLinearLayout1 = findViewById(R.id.jobSpinnerLinearLayout1);
        jobOverviewLinearLayout1 = findViewById(R.id.jobOverviewLinearLayout1);
        descriptionLinearLayout1 = findViewById(R.id.descriptionLinearLayout1);
        jobInfoLinearLayout1 = findViewById(R.id.jobInfoLinearLayout1);
        accidentsLinearLayout1 = findViewById(R.id.accidentsLinearLayout1);
        workReport2 = findViewById(R.id.workReport2);
        workReport3 = findViewById(R.id.workReport3);
        workReport4 = findViewById(R.id.workReport4);
        workReport5 = findViewById(R.id.workReport5);
        addWorkReport = findViewById(R.id.addWorkReport);
        workReport2.setVisibility(View.GONE);
        workReport3.setVisibility(View.GONE);
        workReport4.setVisibility(View.GONE);
        workReport5.setVisibility(View.GONE);
        workReportCounter=1;


        dayTypeSpinner1.setAdapter(new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_item, Type.values()));
        dayTypeSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type1 = (Type)parent.getItemAtPosition(position);
                if (type1==Type.WORK){
                    setWorkReportLayout();
                }
                else if(type1 == Type.DAY_OFF){
                    setDayOffReportLayout();
                }
                else if(type1 == Type.TRAINING){
                    setTrainingReportLayout();
                }
                else if(type1 == Type.BANK_HOLIDAY){
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
        databaseReferenceJobMaintenance=firebaseDatabase.getReference(getString(R.string.firebasepath_job_maintenance));
        databaseReferenceJobService=firebaseDatabase.getReference(getString(R.string.firebasepath_job_service));
        databaseReferenceJobCallOut=firebaseDatabase.getReference(getString(R.string.firebasepath_job_callout));
        jobList.clear();
        jobList.add(null);
        databaseReferenceJob.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){
                    Job jobTemp = ps.getValue(Job.class);
                    jobList.add(jobTemp);
                }

             databaseReferenceJobMaintenance.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshotMaintenance) {
                     for(DataSnapshot ps : dataSnapshotMaintenance.getChildren()){
                         Job jobTemp = ps.getValue(Job.class);
                         jobList.add(jobTemp);
                     }

                     databaseReferenceJobService.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshotService) {
                             for(DataSnapshot ps : dataSnapshotService.getChildren()){
                                 Job jobTemp = ps.getValue(Job.class);
                                 jobList.add(jobTemp);
                             }

                             databaseReferenceJobCallOut.addListenerForSingleValueEvent(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot dataSnapshotCallOut) {
                                     for(DataSnapshot ps : dataSnapshotCallOut.getChildren()){
                                         Job jobTemp = ps.getValue(Job.class);
                                         jobList.add(jobTemp);
                                     }

                                     jobNumberSpinner1 = (Spinner)findViewById(R.id.jobNumberSpinner1);
                                     jobSpinnerAdapter = new JobSpinnerAdapter(DailyReportActivity.this,jobList);
                                     jobNumberSpinner1.setAdapter(jobSpinnerAdapter);
                                     jobNumberSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                         @Override
                                         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                             Job clickedItem = (Job)parent.getItemAtPosition(position);

                                             if (clickedItem!=null){

                                                 job1=clickedItem;

                                                 String overview = "";
                                                 if(job1.getJobNumber()!=null){
                                                     overview=overview+job1.getJobNumber()+" ";
                                                 }
                                                 if(job1.getProjectManager()!=null){
                                                     overview=overview+job1.getProjectManager().getName()+" "+job1.getProjectManager().getSurname()+" ";
                                                 }
                                                 overview = overview+job1.getAddress().getName()+" "
                                                         +job1.getAddress().getStreet()+" "
                                                         +job1.getAddress().getPostCode()+" ";
                                                 if(job1.getJobType()!=null&&job1.getJobType()!=JobType.INSTALLATION){
                                                     overview=overview+job1.getJobType();
                                                 }


                                                 jobOverview1.setText(overview);
                                             }
                                             else{
                                                 job1.setJobNumber("");
                                                 job1.setAddress(new Address("",
                                                         "",
                                                         ""));
                                                 job1.setShortDescription("");
                                                 job1.setProjectManager(new Manager("",
                                                         "",
                                                         ""));
                                                 jobOverview1.setText(job1.getJobNumber()+" "
                                                         +job1.getProjectManager().getName()+" "
                                                         +job1.getProjectManager().getSurname()+" "
                                                         +job1.getAddress().getName()+" "
                                                         +job1.getAddress().getStreet()+" "
                                                         +job1.getAddress().getPostCode());
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
        String jInfo = info1.getText().toString().trim();
        String acc = accidents1.getText().toString().trim();
        String desc = jobDescription1.getText().toString().trim();

        if(type1==Type.BANK_HOLIDAY){
            storeData(startTime,endTime,desc,jInfo,acc);
        }
        else if(type1==Type.DAY_OFF){
            storeData(startTime,endTime,desc,jInfo,acc);
        }
        else if(type1==Type.TRAINING){
                if(!TextUtils.isEmpty(desc)) {
                    desc = jobDescription1.getText().toString().trim();
                    storeData(startTime,endTime,desc,jInfo,acc);
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
        }
        else{
            if(!TextUtils.isEmpty(desc)&&!job1.getAddress().getName().isEmpty()){
                desc = jobDescription1.getText().toString().trim();
                storeData(startTime,endTime,desc,jInfo,acc);
            }else{
                Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
            }
        }



    }
    public void storeData(DateAndTime startTime, DateAndTime endTime, String desc, String jInfo, String acc){
        final DailyReport dailyReport = new DailyReport(startTime,endTime,new WorkReport(type1, job1, desc, jInfo, acc,true));

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
        jobSpinnerLinearLayout1.setVisibility(View.VISIBLE);
        jobOverviewLinearLayout1.setVisibility(View.VISIBLE);
        descriptionLinearLayout1.setVisibility(View.VISIBLE);
        jobInfoLinearLayout1.setVisibility(View.VISIBLE);
        accidentsLinearLayout1.setVisibility(View.VISIBLE);
        addWorkReport.setVisibility(View.VISIBLE);
    }
    public void setDayOffReportLayout(){
        jobSpinnerLinearLayout1.setVisibility(View.GONE);
        jobOverviewLinearLayout1.setVisibility(View.GONE);
        descriptionLinearLayout1.setVisibility(View.GONE);
        jobInfoLinearLayout1.setVisibility(View.GONE);
        accidentsLinearLayout1.setVisibility(View.GONE);
        addWorkReport.setVisibility(View.GONE);
    }
    public void setBankHolidayReportLayout(){
        jobSpinnerLinearLayout1.setVisibility(View.GONE);
        jobOverviewLinearLayout1.setVisibility(View.GONE);
        descriptionLinearLayout1.setVisibility(View.GONE);
        jobInfoLinearLayout1.setVisibility(View.GONE);
        accidentsLinearLayout1.setVisibility(View.GONE);
        addWorkReport.setVisibility(View.GONE);
    }
    public void setTrainingReportLayout(){
        jobSpinnerLinearLayout1.setVisibility(View.GONE);
        jobOverviewLinearLayout1.setVisibility(View.GONE);
        descriptionLinearLayout1.setVisibility(View.VISIBLE);
        jobInfoLinearLayout1.setVisibility(View.GONE);
        accidentsLinearLayout1.setVisibility(View.GONE);
        addWorkReport.setVisibility(View.VISIBLE);
    }

    public void backToMainMenuOnClick(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void addNextWorkReport(View view) {
        if(workReportCounter==1){
            String desc = jobDescription1.getText().toString().trim();
            if(type1==Type.TRAINING){
                if(!TextUtils.isEmpty(desc)) {
                    enableWorkReport2();
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
            }else if(type1==Type.WORK){
                if(!TextUtils.isEmpty(desc)&&!job1.getAddress().getName().isEmpty()){
                    enableWorkReport2();
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
            }else;
        }
    }

    private void enableWorkReport2() {
        workReport2.setVisibility(View.VISIBLE);
        //todo finish
        // to do change spinner type to radio buttons
        // todo only two buttons when work report 2 and above
    }
}

// todo add floating button to add another job
// todo check if first job is complited before adding new one
// TODO change picking up job (filtering), maybe different activity?