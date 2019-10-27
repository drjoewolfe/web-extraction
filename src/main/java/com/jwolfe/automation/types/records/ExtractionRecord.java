package com.jwolfe.automation.types.records;

import java.util.Date;

public abstract class ExtractionRecord {
    protected String recordType;

    protected String name;

    protected Date recordDate;

    public String getRecordType() {
        return recordType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    protected ExtractionRecord() {
        recordType = "Extraction";
    }
}
