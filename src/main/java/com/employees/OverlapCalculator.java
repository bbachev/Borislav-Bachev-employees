package com.employees;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class OverlapCalculator {
    private Map<Integer, List<EmployeeRecord>> map = new HashMap<>();

    public OverlapCalculator(List<EmployeeRecord> records) {
        records.forEach(employeeRecord ->
                this.map.computeIfAbsent(
                        employeeRecord.projectId(),
                        k -> new ArrayList<>()
                ).add(employeeRecord));
    }

    public String calculateMaxCommonPeriod() {
        Map<String, Long> outputMap = new HashMap<>();

        this.map.forEach((key1, value) -> {
            for (int i = 0; i < value.size() - 1; i++) {
                EmployeeRecord employeeRecordCurrent = value.get(i);
                for (int j = i + 1; j < value.size(); j++) {
                    EmployeeRecord employeeRecordNext = value.get(j);

                    int id1 = Math.min(employeeRecordCurrent.id(), employeeRecordNext.id());
                    int id2 = Math.max(employeeRecordCurrent.id(), employeeRecordNext.id());

                    String key = id1 + ":" + id2;

                    LocalDate overlapStart = employeeRecordCurrent.from().isAfter(employeeRecordNext.from())
                            ? employeeRecordCurrent.from()
                            : employeeRecordNext.from();

                    LocalDate overlapEnd = employeeRecordCurrent.to().isBefore(employeeRecordNext.to())
                            ? employeeRecordCurrent.to()
                            : employeeRecordNext.to();

                    long between = ChronoUnit.DAYS.between(overlapStart, overlapEnd);

                    if (between > 0) {
                        outputMap.merge(key, between, Long::sum);
                    }
                }
            }
        });

        return outputMap.entrySet().stream().max(Map.Entry.comparingByValue())
                .map(stringLongEntry -> {
                    String[] split = stringLongEntry.getKey().split(":");
                    return String.join(" ", split) + ", " + stringLongEntry.getValue();
                })
                .orElse("No common period");
    }
}
