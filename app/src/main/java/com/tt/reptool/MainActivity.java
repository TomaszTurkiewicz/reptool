package com.tt.reptool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
