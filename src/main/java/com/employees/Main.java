package com.employees;


import java.util.List;

public class Main {
    public static void main(String[] args) {
        CsvParser csvParser = new CsvParser();
        List<EmployeeRecord> employeeRecords = csvParser.parseFile(args[0]);

        OverlapCalculator overlapCalculator = new OverlapCalculator(employeeRecords);
        System.out.println(overlapCalculator.calculateMaxCommonPeriod());
    }
}