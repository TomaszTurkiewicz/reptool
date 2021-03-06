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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tt.reptool.singletons.JobSingleton;
import com.tt.reptool.fragments.DatePickerFragment;
import com.tt.reptool.fragments.TimeStartPickerFragment;
import com.tt.reptool.javaClasses.DailyReport;
import com.tt.reptool.javaClasses.DateAndTime;
import com.tt.reptool.javaClasses.Job;
import com.tt.reptool.javaClasses.JobType;
import com.tt.reptool.javaClasses.Type;
import com.tt.reptool.javaClasses.WorkReport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

        private static final int START_TIME = 0;
        private static final int END_TIME = 1;
        private TextView startDate, startTime, endTime, jobOverview1, jobOverview2, jobOverview3;
        private EditText jobDescription1, info1, accidents1;
        private EditText jobDescription2, info2, accidents2;
        private EditText jobDescription3, info3, accidents3;


        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference databaseReferenceWeeklyReports;
        private DatabaseReference databaseReferenceAllReports;
        private Calendar calendar, calendarEnd;
        private int flag;
        private List<Job> jobList = new ArrayList<>();
        private Job job1 = new Job();
        private Job job2 = new Job();
        private Job job3 = new Job();
        private Type type1, type2, type3;
        private LinearLayout jobSpinnerLinearLayout1, jobOverviewLinearLayout1, descriptionLinearLayout1,
                jobInfoLinearLayout1, accidentsLinearLayout1,
                jobOverviewLinearLayout2, jobAccidentLinearLayout2, jobInfoLinearLayout2,jobSpinnerLinearLayout2,
                jobOverviewLinearLayout3, jobAccidentLinearLayout3, jobInfoLinearLayout3,jobSpinnerLinearLayout3;

        private LinearLayout workReport2LinearLayout, workReport3LinearLayout, workReport4LinearLayout, workReport5LinearLayout;
        private Button addWorkReport;
        private int workReportCounter;
        private RadioButton radioButtonWork1, radioButtonWork2, radioButtonWork3;
        private WorkReport workReport1 = new WorkReport();
        private WorkReport workReport2 = new WorkReport();
        private WorkReport workReport3 = new WorkReport();





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
        jobSpinnerLinearLayout1 = findViewById(R.id.jobSpinnerLinearLayout1);
        jobOverviewLinearLayout1 = findViewById(R.id.jobOverviewLinearLayout1);
        jobOverviewLinearLayout1.setVisibility(View.GONE);
        descriptionLinearLayout1 = findViewById(R.id.descriptionLinearLayout1);
        jobInfoLinearLayout1 = findViewById(R.id.jobInfoLinearLayout1);
        accidentsLinearLayout1 = findViewById(R.id.accidentsLinearLayout1);
        radioButtonWork1 = findViewById(R.id.workRadioButtonWorkReport1);
        workReport2LinearLayout = findViewById(R.id.workReport2);
        workReport3LinearLayout = findViewById(R.id.workReport3);
        workReport4LinearLayout = findViewById(R.id.workReport4);
        workReport5LinearLayout = findViewById(R.id.workReport5);
        addWorkReport = findViewById(R.id.addWorkReport);
        workReport2LinearLayout.setVisibility(View.GONE);
        workReport3LinearLayout.setVisibility(View.GONE);
        workReport4LinearLayout.setVisibility(View.GONE);
        workReport5LinearLayout.setVisibility(View.GONE);
        workReportCounter=1;
        radioButtonWork1.setChecked(true);
        type1=Type.WORK;


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
        jobList.clear();
        jobList.add(null);



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

        if(workReportCounter==1){
            String jInfo = info1.getText().toString().trim();
            String acc = accidents1.getText().toString().trim();
            String desc = jobDescription1.getText().toString().trim();
            if(type1==Type.BANK_HOLIDAY||type1==Type.DAY_OFF){
                setWorkReport(workReport1,null,null,null,null,type1);
                storeData(startTime,endTime,workReport1,null,null);
            }
            else if(type1==Type.TRAINING){
                if(!TextUtils.isEmpty(desc)) {
                    setWorkReport(workReport1,null,desc,null,null,type1);
                    storeData(startTime,endTime,workReport1,null,null);
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
            }
            else{
                if(!TextUtils.isEmpty(desc)&&!job1.getAddress().getName().isEmpty()){
                    setWorkReport(workReport1,job1,desc,jInfo,acc,type1);
                    storeData(startTime,endTime,workReport1,null,null);
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
            }

        }

        else if(workReportCounter==2){
            String jInfo = info2.getText().toString().trim();
            String acc = accidents2.getText().toString().trim();
            String desc = jobDescription2.getText().toString().trim();
            if(type2==Type.BANK_HOLIDAY||type2==Type.DAY_OFF){
                setWorkReport(workReport2,null,null,null,null,type2);
                storeData(startTime,endTime,workReport1, workReport2,null);
            }
            else if(type2==Type.TRAINING){
                if(!TextUtils.isEmpty(desc)) {
                    setWorkReport(workReport2,null,desc,null,null,type2);
                    storeData(startTime,endTime,workReport1, workReport2,null);
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
            }
            else{
                if(!TextUtils.isEmpty(desc)&&!job2.getAddress().getName().isEmpty()){
                    setWorkReport(workReport2,job2,desc,jInfo,acc,type2);
                    storeData(startTime,endTime,workReport1, workReport2,null);
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
            }

        }
        else if(workReportCounter==3){
            String jInfo = info3.getText().toString().trim();
            String acc = accidents3.getText().toString().trim();
            String desc = jobDescription3.getText().toString().trim();
            if(type3==Type.BANK_HOLIDAY||type3==Type.DAY_OFF){
                setWorkReport(workReport3,null,null,null,null,type3);
                storeData(startTime,endTime,workReport1, workReport2,workReport3);
            }
            else if(type3==Type.TRAINING){
                if(!TextUtils.isEmpty(desc)) {
                    setWorkReport(workReport3,null,desc,null,null,type3);
                    storeData(startTime,endTime,workReport1, workReport2, workReport3);
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
            }
            else{
                if(!TextUtils.isEmpty(desc)&&!job3.getAddress().getName().isEmpty()){
                    setWorkReport(workReport3,job3,desc,jInfo,acc,type3);
                    storeData(startTime,endTime,workReport1, workReport2, workReport3);
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
            }


        }







    }
    public void storeData(DateAndTime startTime, DateAndTime endTime, WorkReport workReport1, WorkReport workReport2, WorkReport workReport3){
        final DailyReport dailyReport = new DailyReport(startTime,endTime,workReport1,workReport2, workReport3);

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
        jobOverviewLinearLayout1.setVisibility(View.GONE);
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
                    workReportCounter=2;
                    setWorkReport(workReport1,null,desc,null,null,type1);
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
            }else if(type1==Type.WORK){
                if(!TextUtils.isEmpty(desc)&&!job1.getAddress().getName().isEmpty()){
                    enableWorkReport2();
                    workReportCounter=2;
                    setWorkReport(workReport1,job1,desc,info1.getText().toString().trim(),accidents1.getText().toString().trim(),type1);
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
            }else;
        }
        else if(workReportCounter==2){
            String desc = jobDescription2.getText().toString().trim();
            if(type2==Type.TRAINING){
                if(!TextUtils.isEmpty(desc)) {
                    enableWorkReport3();
                    workReportCounter=3;
                    setWorkReport(workReport2,null,desc,null,null,type2);
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
            }else if(type2==Type.WORK){
                if(!TextUtils.isEmpty(desc)&&!job2.getAddress().getName().isEmpty()){
                    enableWorkReport3();
                    workReportCounter=3;
                    setWorkReport(workReport2,job2,desc,info2.getText().toString().trim(),accidents2.getText().toString().trim(),type2);
                }else{
                    Toast.makeText(this,R.string.empty_fields,Toast.LENGTH_LONG).show();
                }
            }else;
        }
    }

    private void setWorkReport(WorkReport workReport, Job job, String description, String info, String accident, Type type){
        workReport.setJob(job);
        workReport.setDescription(description);
        workReport.setInfo(info);
        workReport.setAccident(accident);
        workReport.setType(type);
    }

    private void enableWorkReport2() {
        workReport2LinearLayout.setVisibility(View.VISIBLE);
        type2=Type.WORK;
        radioButtonWork2 = findViewById(R.id.workRadioButtonWorkReport2);
        radioButtonWork2.setChecked(true);
        jobOverview2 = findViewById(R.id.jobOverview2);
        jobOverviewLinearLayout2 = findViewById(R.id.jobOverviewLinearLayout2);
        jobOverviewLinearLayout2.setVisibility(View.GONE);
        jobDescription2 = findViewById(R.id.jobDescriptionActivityDailyReport2);
        info2 = findViewById(R.id.jobInfoActivityDailyReport2);
        accidents2 = findViewById(R.id.jobAccidentsActivityDailyReport2);
        jobInfoLinearLayout2=findViewById(R.id.jobInfoLinearLayout2);
        jobAccidentLinearLayout2=findViewById(R.id.accidentsLinearLayout2);
        jobSpinnerLinearLayout2=findViewById(R.id.jobSpinnerLinearLayout2);
    }

    private void enableWorkReport3() {
        workReport3LinearLayout.setVisibility(View.VISIBLE);
        type3=Type.WORK;
        radioButtonWork3 = findViewById(R.id.workRadioButtonWorkReport3);
        radioButtonWork3.setChecked(true);
        jobOverview3 = findViewById(R.id.jobOverview3);
        jobOverviewLinearLayout3 = findViewById(R.id.jobOverviewLinearLayout3);
        jobOverviewLinearLayout3.setVisibility(View.GONE);
        jobDescription3 = findViewById(R.id.jobDescriptionActivityDailyReport3);
        info3 = findViewById(R.id.jobInfoActivityDailyReport3);
        accidents3 = findViewById(R.id.jobAccidentsActivityDailyReport3);
        jobInfoLinearLayout3=findViewById(R.id.jobInfoLinearLayout3);
        jobAccidentLinearLayout3=findViewById(R.id.accidentsLinearLayout3);
        jobSpinnerLinearLayout3=findViewById(R.id.jobSpinnerLinearLayout3);
    }

    public void onRadioButtonClickedWorkReport1(View view) {
        boolean checked = ((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.workRadioButtonWorkReport1:
                if(checked){
                    type1=Type.WORK;
                    setWorkReportLayout();
                }
                break;
            case R.id.trainingRadioButtonWorkReport1:
                if(checked){
                    type1=Type.TRAINING;
                    setTrainingReportLayout();
                }
                break;
            case R.id.dayOffRadioButtonWorkReport1:
                if(checked){
                    type1=Type.DAY_OFF;
                    setDayOffReportLayout();
                }
                break;
            case R.id.bankHolidayRadioButtonWorkReport1:
                if(checked){
                    type1=Type.BANK_HOLIDAY;
                    setBankHolidayReportLayout();
                }
                break;
        }
    }

    public void onRadioButtonClickedWorkReport2(View view) {
        boolean checked = ((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.workRadioButtonWorkReport2:
                if(checked){
                    type2=Type.WORK;
                    setWorkReportLayout2();
                }
                break;
            case R.id.trainingRadioButtonWorkReport2:
                if(checked){
                    type2=Type.TRAINING;
                    setTrainingReportLayout2();
                }
                break;
        }
    }

    private void setTrainingReportLayout2() {
        jobAccidentLinearLayout2.setVisibility(View.GONE);
        jobInfoLinearLayout2.setVisibility(View.GONE);
        jobSpinnerLinearLayout2.setVisibility(View.GONE);
    }

    private void setWorkReportLayout2() {
        jobAccidentLinearLayout2.setVisibility(View.VISIBLE);
        jobInfoLinearLayout2.setVisibility(View.VISIBLE);
        jobSpinnerLinearLayout2.setVisibility(View.VISIBLE);
    }

    public void chooseJobOnClick(View view) {
        Intent intent = new Intent(this,JobsToChoose.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if(workReportCounter==1) {
                job1 = JobSingleton.getInstance().readJobFromSingleton();
                jobOverviewLinearLayout1.setVisibility(View.VISIBLE);
                String overview = "";
                if (job1.getJobNumber() != null) {
                    overview = overview + job1.getJobNumber() + " ";
                }
                if (job1.getProjectManager() != null) {
                    overview = overview + job1.getProjectManager().getName() + " " + job1.getProjectManager().getSurname() + " ";
                }
                overview = overview + job1.getAddress().getName() + " "
                        + job1.getAddress().getStreet() + " "
                        + job1.getAddress().getPostCode() + " ";
                if (job1.getJobType() != null && job1.getJobType() != JobType.INSTALLATION) {
                    overview = overview + job1.getJobType();
                }


                jobOverview1.setText(overview);
            }
            else if(workReportCounter==2){
                job2 = JobSingleton.getInstance().readJobFromSingleton();
                jobOverviewLinearLayout2.setVisibility(View.VISIBLE);
                String overview = "";
                if (job2.getJobNumber() != null) {
                    overview = overview + job2.getJobNumber() + " ";
                }
                if (job2.getProjectManager() != null) {
                    overview = overview + job2.getProjectManager().getName() + " " + job2.getProjectManager().getSurname() + " ";
                }
                overview = overview + job2.getAddress().getName() + " "
                        + job2.getAddress().getStreet() + " "
                        + job2.getAddress().getPostCode() + " ";
                if (job2.getJobType() != null && job2.getJobType() != JobType.INSTALLATION) {
                    overview = overview + job2.getJobType();
                }


                jobOverview2.setText(overview);
            }

            else if(workReportCounter==3){
                job3 = JobSingleton.getInstance().readJobFromSingleton();
                jobOverviewLinearLayout3.setVisibility(View.VISIBLE);
                String overview = "";
                if (job3.getJobNumber() != null) {
                    overview = overview + job3.getJobNumber() + " ";
                }
                if (job3.getProjectManager() != null) {
                    overview = overview + job3.getProjectManager().getName() + " " + job3.getProjectManager().getSurname() + " ";
                }
                overview = overview + job3.getAddress().getName() + " "
                        + job3.getAddress().getStreet() + " "
                        + job3.getAddress().getPostCode() + " ";
                if (job3.getJobType() != null && job3.getJobType() != JobType.INSTALLATION) {
                    overview = overview + job3.getJobType();
                }


                jobOverview3.setText(overview);
            }


        }
    }

    public void onRadioButtonClickedWorkReport3(View view) {
        boolean checked = ((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.workRadioButtonWorkReport3:
                if(checked){
                    type3=Type.WORK;
                    setWorkReportLayout3();
                }
                break;
            case R.id.trainingRadioButtonWorkReport3:
                if(checked){
                    type3=Type.TRAINING;
                    setTrainingReportLayout3();
                }
                break;
        }
    }

    private void setTrainingReportLayout3() {
        jobAccidentLinearLayout3.setVisibility(View.GONE);
        jobInfoLinearLayout3.setVisibility(View.GONE);
        jobSpinnerLinearLayout3.setVisibility(View.GONE);
    }

    private void setWorkReportLayout3() {
        jobAccidentLinearLayout3.setVisibility(View.VISIBLE);
        jobInfoLinearLayout3.setVisibility(View.VISIBLE);
        jobSpinnerLinearLayout3.setVisibility(View.VISIBLE);
    }
}
