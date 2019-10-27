package com.jwolfe.automation.types;

import com.jwolfe.automation.extractors.Extractor;
import com.jwolfe.automation.types.records.ExtractionRecord;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

public class ExtractorResult {
    private Extractor extractor;
    private RunStatus runStatus;
    private Duration totalRunDuration;
    private Duration interventionDuration;
    private List<ExtractionRecord> records;

    public Extractor getExtractor() {
        return extractor;
    }

    public void setExtractor(Extractor extractor) {
        this.extractor = extractor;
    }

    public RunStatus getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(RunStatus runStatus) {
        this.runStatus = runStatus;
    }

    public Duration getTotalRunDuration() {
        return totalRunDuration;
    }

    public void setTotalRunDuration(Duration totalRunDuration) {
        this.totalRunDuration = totalRunDuration;
    }

    public Duration getInterventionDuration() {
        return interventionDuration;
    }

    public Duration getAutomatedRunDuration() {
        if(totalRunDuration == null ) {
            return null;
        }

        if(interventionDuration == null) {
            return totalRunDuration;
        }

        return totalRunDuration.minus(interventionDuration).plus(Duration.ofSeconds(1));
    }

    public void setInterventionDuration(Duration interventionDuration) {
        this.interventionDuration = interventionDuration;
    }

    public List<ExtractionRecord> getRecords() {
        return records;
    }

    public ExtractorResult() {
        this.records = new ArrayList<>();
    }

    public ExtractorResult(Extractor extractor) {
        this();

        this.extractor = extractor;
        this.runStatus = RunStatus.Queued;
    }
}
