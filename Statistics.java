package org.example;
import java.util.List;

public class Statistics {
    public static void printStats(List<Employee> employees) {
        System.out.println("\n--- Ежедневная статистика ---");
        for (Employee e : employees) {
            long totalWork = e.getTotalWorkTime().toHours();
            long idle = e.getIdleTime().toHours();
            long totalTime = e.getTotalTimeAtWork().toHours();

            double efficiency = (double) totalWork / Math.max(1, totalTime) * 100;

            System.out.printf("Сотрудник: %s%n", e.getName());
            System.out.printf("Общее время работы: %d часов%n", totalWork);
            System.out.printf("Время простоя: %d часов%n", idle);
            System.out.printf("Общее время на работе: %d часов%n", totalTime);
            System.out.printf("Эффективность: %.2f%%%n", efficiency);
            System.out.println("------------------------------");
        }
    }
}