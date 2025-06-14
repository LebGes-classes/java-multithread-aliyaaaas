package org.example;

public class Task {
    private String description;
    private int hoursRequired;

    public Task(String description, int hoursRequired) {
        this.description = description;
        this.hoursRequired = hoursRequired;
    }
    public String getDescription() {
        return description;
    }
    public int getHoursRequired() {
        return hoursRequired;
    }
}