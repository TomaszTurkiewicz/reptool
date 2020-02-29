package com.tt.reptool;

/*

Daily report - what kind of job and where was taken

 - time in and out
 - job
 - description (what has been done)
 - any additional info for management
 - any accidents

 */



import java.util.Calendar;


public class DailyReport {

    private Calendar startTime;
    private Calendar endTime;
    private Job job;
    private String description;
    private String info;
    private String accident;
    private int year;
    private int weekNumber;
    private int dayOfWeek;

    public int getYear() {
        return year;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public DailyReport(Calendar startTime,
                       Calendar endTime,
                       Job job,
                       String description,
                       String info,
                       String accident) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.job = job;
        this.description = description;
        this.info = info;
        this.accident = accident;
        this.year = startTime.get(Calendar.YEAR);
        this.weekNumber = startTime.get(Calendar.WEEK_OF_YEAR);
        this.dayOfWeek = startTime.get(Calendar.DAY_OF_WEEK);

    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAccident() {
        return accident;
    }

    public void setAccident(String accident) {
        this.accident = accident;
    }



    // TODO finish this class first!!!

}
