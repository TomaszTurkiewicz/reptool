package com.tt.reptool.javaClasses;

/*

Daily report - what kind of job and where was taken

 - time in and out
 - job
 - description (what has been done)
 - any additional info for management
 - any accidents

 */


public class DailyReport {

    private DateAndTime startTime;
    private DateAndTime endTime;
    private WorkReport workReport;
//    private WorkReport workReport2;
//    private WorkReport workReport3;
//    private WorkReport workReport4;
//    private WorkReport workReport5;

    public DailyReport() {
    }

    public DailyReport(DateAndTime startTime, DateAndTime endTime, WorkReport workReport) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.workReport = workReport;

    }

    public WorkReport getWorkReport() {
        return workReport;
    }

    public void setWorkReport(WorkReport workReport) {
        this.workReport = workReport;
    }

    public DateAndTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateAndTime startTime) {
        this.startTime = startTime;
    }

    public DateAndTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateAndTime endTime) {
        this.endTime = endTime;
    }

    public String dateToString() {
        return startTime.getDay()+"/"+startTime.getMonth()+"/"+startTime.getYear();
    }

    public String showTimeInToString() {
        return startTime.getHour()+":"+startTime.getMinute();
    }

    public String showTimeOutToString() {
        return endTime.getHour()+":"+endTime.getMinute();
    }


    public String reportToString(){
        return dateToString()+"\n"+
                showTimeInToString()+"-"+showTimeOutToString()+"\n"+
                workReport.toString();
    }


}
