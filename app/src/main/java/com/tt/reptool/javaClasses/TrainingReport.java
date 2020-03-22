package com.tt.reptool.javaClasses;

public class TrainingReport {
    private String description;

    public TrainingReport() {
        this.description = "Training";
    }

    public TrainingReport(String description) {
        this.description = "Training: "+ description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = "Training: "+ description;
    }
}
