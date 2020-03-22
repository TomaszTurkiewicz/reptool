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
        this.jobNumber = jobNumber.toUpperCase();
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
