package com.tt.reptool.javaClasses;

public class WorkReport {
    private Job job;
    private String description;
    private String info;
    private String accident;

    public WorkReport() {
    }

    public WorkReport(Job job, String description, String info, String accident) {
        this.job = job;
        this.description = description;
        this.info = info;
        this.accident = accident;
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
}
