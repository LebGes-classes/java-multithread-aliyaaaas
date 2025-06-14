package org.example;

import java.time.Duration;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskProcessor implements Runnable {
    private final Employee employee;
    private final Queue<Task> taskQueue;

    public TaskProcessor(Employee employee, Queue<Task> taskQueue) {
        this.employee = employee;
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            if (task == null) continue;

            int remainingHours = task.getHoursRequired();

            while (remainingHours > 0) {
                int workableToday = Math.min(remainingHours, employee.getMaxWorkHoursPerDay());

                employee.addWorkTime(Duration.ofHours(workableToday));
                employee.addTotalTimeAtWork(Duration.ofHours(employee.getMaxWorkHoursPerDay()));

                long idleHours = employee.getMaxWorkHoursPerDay() - workableToday;
                employee.addIdleTime(Duration.ofHours(idleHours));

                remainingHours -= workableToday;
            }
        }
    }
}