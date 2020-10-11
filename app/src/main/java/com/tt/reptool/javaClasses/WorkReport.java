package com.tt.reptool.javaClasses;

public class WorkReport {
    private boolean isJob;
    private Job job;
    private String description;
    private String info;
    private String accident;
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public WorkReport() {
    }

    public WorkReport(Type type,Job job, String description, String info, String accident, boolean isJob) {
        this.type = type;
        this.job = job;
        this.description=description;
        this.info = info;
        this.accident = accident;
        this.isJob = isJob;
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

    public String toString(){
        if(type==Type.WORK){
            String fullDescription = job.getJobNumber()+" "+job.getAddress().getName()+"\n"+
                    job.getAddress().fullAddress()+"\n"+
                    description;

            if(!accident.isEmpty()){
                fullDescription = fullDescription+"\n"+"Accidents: "+accident;
            }

            if(!info.isEmpty()){
                fullDescription=fullDescription+"\n"+"Info: "+info;
            }

            return fullDescription;
        }
        else if(type==Type.TRAINING){
            return type.name()+ "\n" +description;
        }
        else return type.name();
    }
}
