package com.tt.reptool.singletons;

import com.tt.reptool.javaClasses.Job;

public class JobSingleton {
    private static final JobSingleton instance = new JobSingleton();
    Job job = new Job();
    public static JobSingleton getInstance(){
        return instance;
    }

    public void saveJobToSingleton(Job job){
        this.job=job;
    }

    public Job readJobFromSingleton(){
        return JobSingleton.getInstance().job;
    }

}
