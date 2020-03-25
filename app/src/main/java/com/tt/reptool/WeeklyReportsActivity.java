package com.tt.reptool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tt.reptool.adapters.RecyclerViewAdapterWeeklyReports;
import com.tt.reptool.javaClasses.DailyReport;
import com.tt.reptool.javaClasses.DateAndTime;

import java.util.ArrayList;
import java.util.List;

public class WeeklyReportsActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceWeeklyReport;
    private DatabaseReference databaseReferenceAllReport;
    private List<DailyReport> wRepList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_reports);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceWeeklyReport = firebaseDatabase.getReference(getString(R.string.firebasepath_weekly_reports));
        databaseReferenceAllReport = firebaseDatabase.getReference(getString(R.string.firebasepath_all_reports));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initReportList();
    }

    private void initReportList() {
        wRepList.clear();
        databaseReferenceWeeklyReport.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps : dataSnapshot.getChildren()){

                    DailyReport dr = ps.getValue(DailyReport.class);
                    wRepList.add(dr);
                }
                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewWeeklyReportsActivity);
        RecyclerViewAdapterWeeklyReports adapter = new RecyclerViewAdapterWeeklyReports(this,wRepList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new RecyclerViewAdapterWeeklyReports.OnItemClickListener() {
            @Override
            public void onDeleteWeeklyReportClick(int position) {
                removeWeeklyReport(position);
            }

            @Override
            public void onEditWeeklyReportClick(int position) {

            }
        });
    }

    private void removeWeeklyReport(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WeeklyReportsActivity.this);
        builder.setCancelable(true);
        builder.setTitle(R.string.deleting);
        builder.setMessage(getString(R.string.are_you_sure_you_want_delete)+"\n"+
                wRepList.get(position).dateToString()+" "+wRepList.get(position).getWorkReport().getJob().getAddress().getName()+"?");
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DateAndTime tempDate = wRepList.get(position).getStartTime();
                databaseReferenceWeeklyReport.child(showDateBackwards(tempDate)).removeValue();
                databaseReferenceAllReport.child(showDateBackwards(tempDate)).removeValue();
                initReportList();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public String showDateBackwards (DateAndTime c){
        String format = "%1$02d";

        String month = String.format(format, c.getMonth());
        String day = String.format(format, c.getDay());

        return (c.getYear())+"_"+
                month + "_"+
                day;
    }


    public void sendWeeklyReports(View view) {
        if(wRepList.size()>0){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle(getString(R.string.sending));
            builder.setMessage(getString(R.string.are_you_sure));
            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String subject = "Weekly Report " +
                            wRepList.get(0).dateToString() +
                            " - " +
                            wRepList.get(wRepList.size()-1).dateToString();
                    String message = "Hi"+"\n"+"\n";

                    for(int i = 0; i<wRepList.size();i++){
                        message = message + wRepList.get(i).reportToString()+"\n"+"\n";
                    }

                    message = message + "Kind Regards"+"\n"+"Tomasz Turkiewicz";

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_SUBJECT,subject);
                    intent.putExtra(Intent.EXTRA_TEXT,message);


                    intent.setType("message/rfc822");
                    startActivity(Intent.createChooser(intent, "choose an email client"));
                    databaseReferenceWeeklyReport.removeValue();
                    finish();
                }
            });
            builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();


        }
        else;
    }
}

