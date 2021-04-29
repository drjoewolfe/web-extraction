package com.jwolfe.automation.types.records;

import java.time.LocalDate;
import java.util.Date;

public abstract class ExtractionRecord {
    protected String recordType;

    protected String name;

    protected LocalDate recordDate;

    public String getRecordType() {
        return recordType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    protected ExtractionRecord() {
        recordType = "Extraction";
    }
}
