package com.tt.reptool;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tt.reptool.singletons.JobSingleton;
import com.tt.reptool.javaClasses.Job;

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

/*
*  todo zrobić kolory w aplikacji i lepsza grafika
*   todo zmienić buttony na ładniejsze
*    todo zmienić przycisk "+" na jakiś mniejszy
*     todo jak zrobić żeby było widoczne kiedy wpisujesz w edit text w daily report
*
*
* */