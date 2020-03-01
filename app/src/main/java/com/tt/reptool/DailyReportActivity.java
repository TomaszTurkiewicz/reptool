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

public class DailyReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener


{
        private static final int START_TIME = 0;
        private static final int END_TIME = 1;
        private TextView startDate, startTime, endTime;
        private Calendar calendar, calendarEnd;
        private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);
        startDate = findViewById(R.id.startDate);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        calendar = Calendar.getInstance();
        calendarEnd = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendarEnd.set(Calendar.HOUR_OF_DAY,17);
        calendar.set(Calendar.MINUTE,30);
        calendarEnd.set(Calendar.MINUTE,30);
        flag=0;
        startDate.setText(showDate(calendar));
        startTime.setText(showTime(calendar));
        endTime.setText(showTime(calendarEnd));


        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStartPickerFragment timeEndPicker = new TimeStartPickerFragment();
                timeEndPicker.setHour(calendarEnd.get(Calendar.HOUR_OF_DAY));
                timeEndPicker.setMin(calendarEnd.get(Calendar.MINUTE));
                flag=END_TIME;
                timeEndPicker.show(getSupportFragmentManager(),"time picker");
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStartPickerFragment timeStartPicker = new TimeStartPickerFragment();
                flag=START_TIME;
                timeStartPicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                timeStartPicker.setMin(calendar.get(Calendar.MINUTE));
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

       if(flag==START_TIME) {
           calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
           calendar.set(Calendar.MINUTE, minute);
           startTime.setText(showTime(calendar));
       }else if(flag==END_TIME){
           calendarEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
           calendarEnd.set(Calendar.MINUTE, minute);
           endTime.setText(showTime(calendarEnd));
        }
    }


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
