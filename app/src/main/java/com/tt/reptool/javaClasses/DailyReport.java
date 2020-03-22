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
    private Type type;
    private WorkReport workReport;
    private DayOffReport dayOffReport;
    private BankHolidayReport bankHolidayReport;
    private TrainingReport trainingReport;
//    private Job job;
//    private String description;
//    private String info;
//    private String accident;

    public DailyReport() {
    }

    public Type getType() {
        return type;
    }

    public DailyReport(DateAndTime startTime, DateAndTime endTime, Type type) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        if(type==Type.WORK){
            workReport = new WorkReport();
        }
        else if(type == Type.DAY_OFF){
            dayOffReport = new DayOffReport();
        }
        else if(type == Type.BANK_HOLIDAY){
            bankHolidayReport = new BankHolidayReport();
        }
        else if(type == Type.TRAINING){
            trainingReport = new TrainingReport();
        }
    }

    public void setWorkReport(WorkReport workReport) {
        if(type==Type.WORK){
        this.workReport = workReport;
        }
        else{
            this.workReport=null;
        }
    }



    public WorkReport getWorkReport() {
        if(type==Type.WORK){
        return workReport;
        }
        else return null;
    }

    public DayOffReport getDayOffReport() {
        if(type==Type.DAY_OFF){
            return dayOffReport;
        }
        else return null;
    }

    public BankHolidayReport getBankHolidayReport(){
        if(type==Type.BANK_HOLIDAY){
            return bankHolidayReport;
        }
        else return null;
    }
    public TrainingReport getTrainingReport(){
        if(type==Type.TRAINING){
            return trainingReport;
        }
        else return null;
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
