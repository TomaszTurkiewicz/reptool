package com.tt.reptool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DailyReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
        private TextView startDate, startTime;
        private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);
        startDate = findViewById(R.id.startDate);
        startTime = findViewById(R.id.startTime);
        calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        startDate.setText(currentDate);
        startTime.setText("choose time");

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeStartPicker = new TimeStartPickerFragment();
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

    //TODO dlaczego time picker nie działa

    @Override
    public void onDateSet(DatePicker view, int yearPicker, int monthPicker, int dayOfMonthPicker) {

        calendar.set(yearPicker,monthPicker,dayOfMonthPicker);
        String date = DateFormat.getDateInstance().format(calendar.getTime());
        startDate.setText(date);

    }
}
