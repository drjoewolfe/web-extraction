package com.jwolfe.automation.types;

import com.jwolfe.automation.types.records.ExtractionRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Extract {
    private Date extractDate;
    private int version;
    private List<ExtractionRecord> records;

    public Date getExtractDate() {
        return extractDate;
    }

    public void setExtractDate(Date extractDate) {
        this.extractDate = extractDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<ExtractionRecord> getRecords() {
        return records;
    }

    public Extract() {
        this.records = new ArrayList<>();
    }
}
