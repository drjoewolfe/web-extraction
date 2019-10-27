package com.jwolfe.automation.types;

import com.jwolfe.automation.extractors.Extractor;

import java.util.*;

public class ExtractionRunSummary {
    private Extract extract;
    // private List<ExtractorResult> extractorResults;
    private Map<String, ExtractorResult> extractorResultMap;
    private List<Extractor> extractorsToAttempt;

    public Extract getExtract() {
        return extract;
    }

    public Collection<ExtractorResult> getExtractorResults() {
        return extractorResultMap.values();
    }

    public Map<String, ExtractorResult> getExtractorResultMap() {
        return extractorResultMap;
    }

    public void setExtractorResultMap(Map<String, ExtractorResult> extractorResultMap) {
        this.extractorResultMap = extractorResultMap;
    }

    public List<Extractor> getExtractorsToAttempt() {
        return extractorsToAttempt;
    }

    public int getCountOfExtractorsAttempted() {
        return extractorsToAttempt.size();
    }

    public long getCountOfExtractorsRan() {
        return this.extractorResultMap.values().stream()
                .filter(extractor -> extractor.getRunStatus() != RunStatus.Queued)
                .count();
    }

    public int getCountOfExtractorsSucceeded() {
        return (int) this.extractorResultMap.values().stream()
                .filter(extractor -> extractor.getRunStatus() == RunStatus.Succeeded)
                .count();
    }

    public int getCountOfExtractorsFailed() {
       return (int) this.extractorResultMap.values().stream()
                .filter(extractor -> extractor.getRunStatus() == RunStatus.Failed)
                .count();
    }

    public long getTotalExtractionTimeInSeconds() {
        return this.extractorResultMap.values().stream()
                .map(result -> {
                    var duration = result.getTotalRunDuration();
                    return  duration == null ? 0 : duration.toSeconds();
                })
                .reduce((a, b) -> a + b)
                .orElse(0L);
    }

    public int getTotalRecordsExtracted() {
        return this.extractorResultMap.values().stream()
                .map(result -> result.getRecords().size())
                .reduce((a, b) -> a + b)
                .orElse(0);
    }

    public ExtractionRunSummary() {
        this.extract = new Extract();
        this.extractorResultMap = new LinkedHashMap<>();

        this.extractorsToAttempt = new ArrayList<>();
    }
}
