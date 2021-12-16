package com.jwolfe.automation.extractors;

import com.jwolfe.automation.types.ExtractorDefinition;
import com.jwolfe.automation.types.ExtractorResult;
import com.jwolfe.ankyl.commons.core.CheckedConsumer;
import com.jwolfe.automation.types.records.ExtractionRecord;
import org.openqa.selenium.WebDriver;

import java.io.InterruptedIOException;
import java.util.List;
import java.util.function.Consumer;

public interface Extractor {
    String getName();

    String getDescription();

    String getCategory();

    String getFamily();

    ExtractorDefinition getDefinition();

    boolean isInteractionRequired();

    List<ExtractionRecord> getRecords(WebDriver driver) throws InterruptedException, InterruptedIOException;

    List<ExtractionRecord> getRecords(WebDriver driver, ExtractorResult result) throws InterruptedException, InterruptedIOException;

    ExtractorResult run(WebDriver driver);

    ExtractorResult run(WebDriver driver, ExtractorResult result);

    void registerUserInteractionRequestCallback(CheckedConsumer<String> callback);

    void registerExtractionProgressCallback(Consumer<ExtractorResult> callback);

    void clearExtractionProgressCallbacks();
}
