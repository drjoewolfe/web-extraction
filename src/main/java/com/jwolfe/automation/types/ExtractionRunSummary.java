package com.jwolfe.automation.types;

import com.jwolfe.automation.extractors.Extractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExtractionRunSummary {
    private Extract extract;
    // private List<ExtractorResult> extractorResults;
    private Map<ExtractorDefinition, ExtractorResult> extractorResultMap;
    private List<Extractor> extractorsToAttempt;

    public Extract getExtract() {
        return extract;
    }

    public Collection<ExtractorResult> getExtractorResults() {
        return extractorResultMap.values();
    }

    public Map<ExtractorDefinition, ExtractorResult> getExtractorResultMap() {
        return extractorResultMap;
    }

    public void setExtractorResultMap(Map<ExtractorDefinition, ExtractorResult> extractorResultMap) {
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

    public List<ExtractorResult> getExtractorsFailed() {
        return this.extractorResultMap.values().stream()
                .filter(extractor -> extractor.getRunStatus() == RunStatus.Failed)
                .collect(Collectors.toList());
    }

    public int getCountOfExtractorsFailed() {
       return (int) this.extractorResultMap.values().stream()
                .filter(extractor -> extractor.getRunStatus() == RunStatus.Failed)
                .count();
    }

    public int getCountOfExtractorsInProgress() {
        return (int) this.extractorResultMap.values().stream()
                .filter(extractor -> extractor.getRunStatus() == RunStatus.Running)
                .count();
    }

    public int getCountOfExtractorsPartiallySucceeded() {
        return (int) this.extractorResultMap.values().stream()
                .filter(extractor -> extractor.getRunStatus() == RunStatus.Partial)
                .count();
    }

    public int getCountOfExtractorsQueued() {
        return (int) this.extractorResultMap.values().stream()
                .filter(extractor -> extractor.getRunStatus() == RunStatus.Queued)
                .count();
    }

    public long getProgressPercent() {
        long progressPercent = 0;
        if(getCountOfExtractorsAttempted() != 0) {
            progressPercent = (getCountOfExtractorsRan() - getCountOfExtractorsInProgress()) * 100 / getCountOfExtractorsAttempted();

        }

        return progressPercent;
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

    public long getAutomatedExtractionTimeInSeconds() {
        return this.extractorResultMap.values().stream()
                .map(result -> {
                    var duration = result.getAutomatedRunDuration();
                    return  duration == null ? 0 : duration.toSeconds();
                })
                .reduce((a, b) -> a + b)
                .orElse(0L);
    }

    public long getManualExtractionTimeInSeconds() {
        return this.extractorResultMap.values().stream()
                .map(result -> {
                    var duration = result.getInterventionDuration();
                    return  duration == null ? 0 : duration.toSeconds();
                })
                .reduce((a, b) -> a + b)
                .orElse(0L);
    }

    public float getTotalExtractionTimeInMinutes() {
        return getTotalExtractionTimeInSeconds() / 60f;
    }

    public float getAutomatedExtractionTimeInMinutes() {
        return getAutomatedExtractionTimeInSeconds() / 60f;
    }

    public float getManualExtractionTimeInMinutes() {
        return getManualExtractionTimeInSeconds() / 60f;
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
