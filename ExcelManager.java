package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ExcelManager {

    public static List<Employee> readEmployeesFromExcel(String filePath) throws IOException {
        List<Employee> employees = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet("Employees");

        if (sheet == null) {
            throw new RuntimeException("Лист 'Employees' не найден в файле");
        }

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // заголовок
            Cell nameCell = row.getCell(0);
            if (nameCell != null && nameCell.getCellType() == CellType.STRING) {
                employees.add(new Employee(nameCell.getStringCellValue()));
            }
        }

        workbook.close();
        fis.close();
        return employees;
    }

    public static Map<String, Queue<Task>> readTasksByEmployeeFromExcel(String filePath) throws IOException {
        Map<String, Queue<Task>> tasksByEmployee = new HashMap<>();
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet taskSheet = workbook.getSheet("Tasks");

        if (taskSheet == null) {
            throw new RuntimeException("Лист 'Tasks' не найден в файле");
        }

        for (Row row : taskSheet) {
            if (row.getRowNum() == 0) continue; // заголовок

            Cell descCell = row.getCell(0);
            Cell hoursCell = row.getCell(1);
            Cell employeeCell = row.getCell(2);

            if (descCell != null && hoursCell != null && employeeCell != null &&
                    hoursCell.getCellType() == CellType.NUMERIC &&
                    employeeCell.getCellType() == CellType.STRING) {

                String description = descCell.getStringCellValue();
                int hours = (int) hoursCell.getNumericCellValue();
                String employeeName = employeeCell.getStringCellValue();

                tasksByEmployee.computeIfAbsent(employeeName, k -> new ConcurrentLinkedQueue<>())
                        .add(new Task(description, hours));
            }
        }

        workbook.close();
        fis.close();
        return tasksByEmployee;
    }
    public static void writeResultsToExcel(List<Employee> employees, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Results");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        headerRow.createCell(1).setCellValue("Total Work Time (hours)");
        headerRow.createCell(2).setCellValue("Idle Time (hours)");
        headerRow.createCell(3).setCellValue("Total Time at Work (hours)");

        int rowNum = 1;
        for (Employee employee : employees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(employee.getName());
            row.createCell(1).setCellValue(employee.getTotalWorkTime().toHours());
            row.createCell(2).setCellValue(employee.getIdleTime().toHours());
            row.createCell(3).setCellValue(employee.getTotalTimeAtWork().toHours());
        }
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }

        workbook.close();
    }
}