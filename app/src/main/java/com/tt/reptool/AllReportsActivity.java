package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
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
import com.tt.reptool.javaClasses.Job;
import com.tt.reptool.javaClasses.Manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class AllReportsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<DailyReport> rList = new ArrayList<>();
    private TextView dateTextView;
    private Spinner jobSpinner;
    private Calendar calendar;
    private DatabaseReference databaseReferenceJob;
    private List<Job> jobList = new ArrayList<>();
    private JobSpinnerAdapter jobSpinnerAdapter;
    private Job job = new Job();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reports);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(getString(R.string.firebasepath_all_reports));
        databaseReferenceJob=firebaseDatabase.getReference(getString(R.string.firebasepath_job));
        dateTextView = findViewById(R.id.startDateAllReport);
        jobSpinner = findViewById(R.id.jobNumberSpinnerAllReports);
        initReportList();
        calendar = Calendar.getInstance();
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        jobList.clear();
        jobList.add(null);
        databaseReferenceJob.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ps : dataSnapshot.getChildren()) {
                    Job jobTemp = ps.getValue(Job.class);
                    jobList.add(jobTemp);
                }
                jobSpinnerAdapter = new JobSpinnerAdapter(AllReportsActivity.this,jobList);
                jobSpinner.setAdapter(jobSpinnerAdapter);
                jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Job clickedItem = (Job)parent.getItemAtPosition(position);
                        if (clickedItem!=null) {
                            job.setJobNumber(clickedItem.getJobNumber());
                            job.setAddress(new Address(clickedItem.getAddress().getName(),
                                    clickedItem.getAddress().getStreet(),
                                    clickedItem.getAddress().getPostCode()));
                            job.setShortDescription(clickedItem.getShortDescription());
                            job.setProjectManager(new Manager(clickedItem.getProjectManager().getName(),
                                    clickedItem.getProjectManager().getSurname(),
                                    clickedItem.getProjectManager().getEmailAddress()));
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




    private String showDate(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH)+"/"+
                (calendar.get(Calendar.MONTH)+1)+"/"+
                calendar.get(Calendar.YEAR);
    }

    private void initReportList() {
        rList.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){


                    DailyReport dr = ps.getValue(DailyReport.class);

                    rList.add(dr);
                }
                Collections.reverse(rList);
                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAllReportsActivity);
        RecyclerViewAdapterReport adapter = new RecyclerViewAdapterReport(this,rList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(year,month,dayOfMonth);
        dateTextView.setText(showDate(calendar));
    }
}
