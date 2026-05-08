package com.employees;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    private static final List<DateTimeFormatter> FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy")
    );

    private LocalDate parseDate(String date) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
            }
        }
        throw new RuntimeException("Unsupported date format: " + date);
    }


    public List<EmployeeRecord> parseFile(String filePath){
        List<EmployeeRecord> records = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            reader.skip(1);
            while ((line = reader.readNext()) != null) {
                EmployeeRecord record = new EmployeeRecord(
                        Integer.parseInt(line[0].trim()),
                        Integer.parseInt(line[1].trim()),
                        parseDate(line[2].trim()),
                        line[3].trim().equalsIgnoreCase("NULL") ? LocalDate.now() : parseDate(line[3].trim())
                );
                records.add(record);
            }
            return records;
        } catch (CsvValidationException | IOException e)  {
            throw new RuntimeException(e);
        }
    }
}
