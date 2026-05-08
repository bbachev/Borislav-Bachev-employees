package com.employees;

import java.time.LocalDate;

public record EmployeeRecord(Integer id, Integer projectId, LocalDate from, LocalDate to) {
}
