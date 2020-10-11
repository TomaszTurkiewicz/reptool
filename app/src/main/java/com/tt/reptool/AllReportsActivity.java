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
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tt.reptool.adapters.JobSpinnerAdapter;
import com.tt.reptool.adapters.RecyclerViewAdapterReport;
import com.tt.reptool.fragments.DatePickerFragment;
import com.tt.reptool.javaClasses.Address;
import com.tt.reptool.javaClasses.DailyReport;
import com.tt.reptool.javaClasses.DateAndTime;
import com.tt.reptool.javaClasses.Job;
import com.tt.reptool.javaClasses.JobType;
import com.tt.reptool.javaClasses.Manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class AllReportsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<DailyReport> rList = new ArrayList<>();
    private List<DailyReport> rFilterList = new ArrayList<>();
    private TextView dateTextView;
    private Spinner jobSpinner;
    private Calendar calendar;
    private DatabaseReference databaseReferenceJob;
    private DatabaseReference databaseReferenceMaintenance;
    private DatabaseReference databaseReferenceService;
    private DatabaseReference databaseReferenceCallOut;
    private List<Job> jobList = new ArrayList<>();
    private JobSpinnerAdapter jobSpinnerAdapter;
    private Job job = new Job();
    private String jobNumber;
    private DateAndTime date = new DateAndTime();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reports);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(getString(R.string.firebasepath_all_reports));
        databaseReferenceJob=firebaseDatabase.getReference(getString(R.string.firebasepath_job));
        databaseReferenceMaintenance=firebaseDatabase.getReference(getString(R.string.firebasepath_job_maintenance));
        databaseReferenceService=firebaseDatabase.getReference(getString(R.string.firebasepath_job_service));
        databaseReferenceCallOut=firebaseDatabase.getReference(getString(R.string.firebasepath_job_callout));
        dateTextView = findViewById(R.id.startDateAllReport);
        jobSpinner = findViewById(R.id.jobNumberSpinnerAllReports);
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
                                        }
                                        jobSpinnerAdapter = new JobSpinnerAdapter(AllReportsActivity.this,jobList);
                                        jobSpinner.setAdapter(jobSpinnerAdapter);
                                        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                Job clickedItem = (Job)parent.getItemAtPosition(position);
                                                if (clickedItem!=null) {

                                                    job=clickedItem;


//                                                    job.setJobNumber(clickedItem.getJobNumber());
//                                                    job.setAddress(new Address(clickedItem.getAddress().getName(),
//                                                            clickedItem.getAddress().getStreet(),
//                                                            clickedItem.getAddress().getPostCode()));
//                                                    job.setShortDescription(clickedItem.getShortDescription());
//                                                    job.setProjectManager(new Manager(clickedItem.getProjectManager().getName(),
//                                                            clickedItem.getProjectManager().getSurname(),
//                                                            clickedItem.getProjectManager().getEmailAddress()));
//                                                    jobNumber=clickedItem.getJobNumber();
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
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){


                    DailyReport dr = ps.getValue(DailyReport.class);

                    list.add(dr);
                }
                Collections.reverse(list);
                initRecyclerView(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRecyclerView(List<DailyReport> list) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAllReportsActivity);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
        RecyclerViewAdapterReport adapter = new RecyclerViewAdapterReport(this,list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            if(job.getJobType()==JobType.INSTALLATION||job.getJobType()==null){
                for(int i = 0; i<rList.size();i++){
                    if((rList.get(i).getWorkReport().getJob().getJobType()==JobType.INSTALLATION||
                            rList.get(i).getWorkReport().getJob().getJobType()==null)&&
                            rList.get(i).getWorkReport().getJob().getJobNumber().equals(job.getJobNumber())){
                        rFilterList.add(rList.get(i));
                    }
                }
            }else if(job.getJobType()==JobType.MAINTENANCE){
                for(int i = 0; i<rList.size();i++){
                    if(rList.get(i).getWorkReport().getJob().getJobType()==JobType.MAINTENANCE&&
                            rList.get(i).getWorkReport().getJob().getAddress().getName().equals(job.getAddress().getName()))
                    {
                        rFilterList.add(rList.get(i));
                    }
                }
            }
            else if(job.getJobType()==JobType.SERVICE){
                for(int i = 0; i<rList.size();i++){
                    if(rList.get(i).getWorkReport().getJob().getJobType()==JobType.SERVICE&&
                            rList.get(i).getWorkReport().getJob().getAddress().getName().equals(job.getAddress().getName()))
                    {
                        rFilterList.add(rList.get(i));
                    }
                }
            }
            else if(job.getJobType()==JobType.CALL_OUT){
                for(int i = 0; i<rList.size();i++){
                    if(rList.get(i).getWorkReport().getJob().getJobType()==JobType.CALL_OUT&&
                            rList.get(i).getWorkReport().getJob().getAddress().getName().equals(job.getAddress().getName()))
                    {
                        rFilterList.add(rList.get(i));
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
        initRecyclerView(rFilterList);
    }
}
