package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tt.reptool.adapters.JobSpinnerAdapter;
import com.tt.reptool.adapters.RecyclerViewAdapterReport;
import com.tt.reptool.fragments.DatePickerFragment;
import com.tt.reptool.javaClasses.DailyReport;
import com.tt.reptool.javaClasses.DateAndTime;
import com.tt.reptool.javaClasses.Job;
import com.tt.reptool.javaClasses.JobType;
import com.tt.reptool.javaClasses.Type;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class AllReportsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private DatabaseReference databaseReferenceAllReports;
    private final List<DailyReport> rList = new ArrayList<>();
    private List<DailyReport> rFilterList = new ArrayList<>();
    private TextView dateTextView;
    private Spinner jobSpinner;
    private Calendar calendar;
    private DatabaseReference databaseReferenceMaintenance;
    private DatabaseReference databaseReferenceService;
    private DatabaseReference databaseReferenceCallOut;
    private List<Job> jobList = new ArrayList<>();
    private JobSpinnerAdapter jobSpinnerAdapter;
    private Job job = new Job();
    private DateAndTime date = new DateAndTime();
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reports);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceAllReports = firebaseDatabase.getReference(getString(R.string.firebasepath_all_reports));
        DatabaseReference databaseReferenceJob = firebaseDatabase.getReference(getString(R.string.firebasepath_job));
        databaseReferenceMaintenance= firebaseDatabase.getReference(getString(R.string.firebasepath_job_maintenance));
        databaseReferenceService= firebaseDatabase.getReference(getString(R.string.firebasepath_job_service));
        databaseReferenceCallOut= firebaseDatabase.getReference(getString(R.string.firebasepath_job_callout));
        dateTextView = findViewById(R.id.startDateAllReport);
        jobSpinner = findViewById(R.id.jobNumberSpinnerAllReports);
        recyclerView = findViewById(R.id.recyclerViewAllReportsActivity);
        final DialogFragment datePicker = new DatePickerFragment();
        initReportList(rList);
        calendar = Calendar.getInstance();
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        jobList.clear();
        jobList.add(null);
        databaseReferenceJob.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for (DataSnapshot ps : dataSnapshot.getChildren()) {
                    Job jobTemp = ps.getValue(Job.class);
                    jobList.add(jobTemp);
                }

                databaseReferenceMaintenance.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshotMaintenance) {
                        for (DataSnapshot psm : dataSnapshotMaintenance.getChildren()){
                            Job jobTemp = psm.getValue(Job.class);
                            jobList.add(jobTemp);
                        }

                        databaseReferenceService.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshotService) {
                                for(DataSnapshot pss:dataSnapshotService.getChildren()){
                                    Job jobTemp = pss.getValue(Job.class);
                                    jobList.add(jobTemp);
                                }
                                databaseReferenceCallOut.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshotCallout) {
                                        for(DataSnapshot psc : dataSnapshotCallout.getChildren()){
                                            Job jobTemp = psc.getValue(Job.class);
                                            jobList.add(jobTemp);
                                        }
                                        jobSpinnerAdapter = new JobSpinnerAdapter(AllReportsActivity.this,jobList);
                                        jobSpinner.setAdapter(jobSpinnerAdapter);
                                        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                Job clickedItem = (Job)parent.getItemAtPosition(position);
                                                if (clickedItem!=null) {

                                                    job=clickedItem;

                                                    date.setYear(0);
                                                    date.setMonth(0);
                                                    date.setMinute(0);
                                                    dateTextView.setText(getString(R.string.choose_date));
                                                    reloadList();
                                                }else{

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

    }




    private String showDate(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH)+"/"+
                (calendar.get(Calendar.MONTH)+1)+"/"+
                calendar.get(Calendar.YEAR);
    }

    private void initReportList(final List<DailyReport> list) {
        list.clear();
        databaseReferenceAllReports.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){


                    DailyReport dr = ps.getValue(DailyReport.class);

                    list.add(dr);
                }
                Collections.reverse(list);
                loadDataToRecyclerView(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadDataToRecyclerView(List<DailyReport> list) {
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        RecyclerViewAdapterReport adapter = new RecyclerViewAdapterReport(AllReportsActivity.this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllReportsActivity.this));
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(year,month,dayOfMonth);
        dateTextView.setText(showDate(calendar));
        jobSpinner.setSelection(0);
        date.setDateAndTime(calendar);
        reloadList();
    }

    private void reloadList() {
        rFilterList.clear();

        if(date.getYear()==0){
            DailyReport drtemp = new DailyReport();
            // INSTALLATION
            if(job.getJobType()==null||job.getJobType().equals(JobType.INSTALLATION)) {
                for (int i = 0; i < rList.size(); i++) {
                    drtemp = rList.get(i);
                    // work report first, check if work (not training, day off or bank holiday)
                    if(drtemp.getWorkReport().getType().equals(Type.WORK)){

                        // check if there is a job type at all, or job type is installation
                        if(drtemp.getWorkReport().getJob().getJobType()==null||
                                drtemp.getWorkReport().getJob().getJobType().equals(JobType.INSTALLATION)){

                            // compare job number
                            if(drtemp.getWorkReport().getJob().getJobNumber().equals(job.getJobNumber())){
                             rFilterList.add(drtemp);
                            }
                        }
                    }

                    // work report second exists
                    if(drtemp.getWorkReport2()!=null){
                        // work not training
                        if(drtemp.getWorkReport2().getType().equals(Type.WORK)){
                            // check job type (null or installation)
                            if(drtemp.getWorkReport2().getJob().getJobType()==null||
                                    drtemp.getWorkReport2().getJob().getJobType().equals(JobType.INSTALLATION)){
                                // compare job number
                                if(drtemp.getWorkReport2().getJob().getJobNumber().equals(job.getJobNumber())){
                                    rFilterList.add(drtemp);
                                }
                            }
                        }
                    }
                }
            }
            // MAINTENANCE, SERVICE, CALL OUT
            else{
                for (int i = 0; i < rList.size(); i++){
                    drtemp = rList.get(i);
                    // work report first
                    if(drtemp.getWorkReport().getType().equals(Type.WORK)){
                        if(drtemp.getWorkReport().getJob().getJobType()!=null&&
                                drtemp.getWorkReport().getJob().getJobType().equals(job.getJobType())){
                            if(drtemp.getWorkReport().getJob().getAddress().fullAddress().equals(job.getAddress().fullAddress())){
                                rFilterList.add(drtemp);
                            }
                        }
                    }

                    // work report second
                    if(drtemp.getWorkReport2()!=null){
                        if(drtemp.getWorkReport2().getType().equals(Type.WORK)){
                            if(drtemp.getWorkReport2().getJob().getJobType()!=null&&
                                    drtemp.getWorkReport2().getJob().getJobType().equals(job.getJobType())){
                                if(drtemp.getWorkReport2().getJob().getAddress().fullAddress().equals(job.getAddress().fullAddress())){
                                    rFilterList.add(drtemp);
                                }
                            }
                        }
                    }
                }
            }
        }
        else{
            for(int i = 0; i<rList.size();i++){
                if(rList.get(i).getStartTime().getYear()==date.getYear()&&
                rList.get(i).getStartTime().getMonth()==date.getMonth()&&
                rList.get(i).getStartTime().getDay()==date.getDay()){
                    rFilterList.add(rList.get(i));
                }
            }
        }
        loadDataToRecyclerView(rFilterList);
    }
}
