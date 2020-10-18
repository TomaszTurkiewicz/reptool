package com.tt.reptool;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tt.reptool.Singletons.JobSingleton;
import com.tt.reptool.javaClasses.Job;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    public final static int REQ_CODE_CHILD = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textviewmainactivity);
        tv.setText("NONE");
    }


    public void onManagerClick(View view) {
        Intent intent = new Intent(this, ManagerActivity.class);
        startActivity(intent);
    }

    public void onJobClick(View view) {
        Intent intent = new Intent(this, JobActivity.class);
        startActivity(intent);

    }

    public void dailyReportOnClick(View view) {
        Intent intent = new Intent(this,DailyReportActivity.class);
        startActivity(intent);
        finish();
    }

    public void allReportsActivityClick(View view) {
        Intent intent = new Intent(this,AllReportsActivity.class);
        startActivity(intent);
    }

    public void weeklyReportOnClick(View view) {
        Intent intent = new Intent(this,WeeklyReportsActivity.class);
        startActivity(intent);
        finish();
    }


    public void goToJobToChoose(View view) {
        Intent intent = new Intent(this,JobsToChoose.class);
        startActivityForResult(intent,REQ_CODE_CHILD);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {

          if (requestCode == REQ_CODE_CHILD&&resultCode==RESULT_OK) {
            Job job = JobSingleton.getInstance().readJobFromSingleton();

              tv.setText(job.getJobNumber());
          }
    }
}
