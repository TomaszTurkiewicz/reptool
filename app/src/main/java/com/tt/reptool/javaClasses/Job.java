package com.tt.reptool.javaClasses;

/*

Job:

 - job number
 - address
 - project manager

 */


public class Job {

    private String jobNumber;
    private Address address;
    private String shortDescription;
    private Manager projectManager;
    private JobType jobType;
    private boolean isFinished;

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public Job() {
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        if(jobNumber!=null) {
            this.jobNumber = jobNumber.toUpperCase();
        }
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Manager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(Manager projectManager) {
        this.projectManager = projectManager;
    }


}
