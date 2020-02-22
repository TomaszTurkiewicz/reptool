package com.tt.reptool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FindJobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_job);
    }

    public void showAllOnClick(View view) {
        Intent intent = new Intent(this, AllJobs.class);
        startActivity(intent);
    }

    public void findJobWithCriteria(View view) {
    }
}
