package org.example;

import java.time.Duration;

public class Employee {
    private String name;
    private int maxWorkHoursPerDay = 8;
    private Duration totalWorkTime = Duration.ZERO;
    private Duration idleTime = Duration.ZERO;
    private Duration totalTimeAtWork = Duration.ZERO;

    public Employee(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public int getMaxWorkHoursPerDay() {
        return maxWorkHoursPerDay;
    }
    public void addWorkTime(Duration duration) {
        totalWorkTime = totalWorkTime.plus(duration);
    }
    public void addIdleTime(Duration duration) {
        idleTime = idleTime.plus(duration);
    }
    public void addTotalTimeAtWork(Duration duration) {
        totalTimeAtWork = totalTimeAtWork.plus(duration);
    }
    public Duration getTotalWorkTime() {
        return totalWorkTime;
    }
    public Duration getIdleTime() {
        return idleTime;
    }
    public Duration getTotalTimeAtWork() {
        return totalTimeAtWork;
    }
}