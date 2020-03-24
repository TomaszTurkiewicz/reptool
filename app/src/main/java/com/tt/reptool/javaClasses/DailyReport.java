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

//    private Job job;
//    private String description;
//    private String info;
//    private String accident;

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

    /*   public DailyReport(DateAndTime startTime,
                                       DateAndTime endTime,
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
                    }
                */
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

  /*  public Job getJob() {
        return job;
    }
*/
  /*
    public void setJob(Job job) {
        this.job = job;
    }
*/
  /*
    public String getDescription() {
        return description;
    }
*/
  /*
    public void setDescription(String description) {
        this.description = description;
    }
*/
  /*
    public String getInfo() {
        return info;
    }
*/
  /*
    public void setInfo(String info) {
        this.info = info;
    }
*/
  /*
    public String getAccident() {
        return accident;
    }
*/
  /*
    public void setAccident(String accident) {
        this.accident = accident;
    }
*/
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
        return "dupa";
    }


}
