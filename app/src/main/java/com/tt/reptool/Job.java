package com.tt.reptool;

/*

Job:

 - job number
 - address
 - project manager

 */


public class Job {

    private String jobNumber;
    private String address;
    private Manager projectManager;

    public Job() {
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Manager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(Manager projectManager) {
        this.projectManager = projectManager;
    }
}
