package com.tt.reptool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DailyReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
        private TextView startDate, startTime;
        private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);
        startDate = findViewById(R.id.startDate);
        startTime = findViewById(R.id.startTime);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,30);
        startDate.setText(showDate(calendar));
        startTime.setText(showTime(calendar));


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

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);

        startTime.setText(showTime(calendar));
    }

    //TODO dlaczego time picker nie działa

    @Override
    public void onDateSet(DatePicker view, int yearPicker, int monthPicker, int dayOfMonthPicker) {

        calendar.set(Calendar.YEAR,yearPicker);
        calendar.set(Calendar.MONTH,monthPicker);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonthPicker);

        startDate.setText(showDate(calendar));

    }

    public String showDate(Calendar c){
        return c.get(Calendar.DAY_OF_MONTH)+"/"+
                (c.get(Calendar.MONTH)+1)+"/"+
                c.get(Calendar.YEAR);
    }

    public String showTime(Calendar c){
        return c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
    }
}
