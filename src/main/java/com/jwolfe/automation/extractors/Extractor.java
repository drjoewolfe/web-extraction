package com.jwolfe.automation.extractors;

import com.jwolfe.automation.types.ExtractorResult;
import com.jwolfe.automation.types.core.CheckedConsumer;
import com.jwolfe.automation.types.records.ExtractionRecord;
import org.openqa.selenium.WebDriver;

import java.io.InterruptedIOException;
import java.util.List;
import java.util.function.Consumer;

public interface Extractor {
    String getName();

    String getDescription();

    String getCategory();

    boolean isInteractionRequired();

    List<ExtractionRecord> GetRecords(WebDriver driver) throws InterruptedException, InterruptedIOException;

    List<ExtractionRecord> GetRecords(WebDriver driver, ExtractorResult result) throws InterruptedException, InterruptedIOException;

    ExtractorResult Run(WebDriver driver);

    ExtractorResult Run(WebDriver driver, ExtractorResult result);

    void RegisterUserInteractionRequestCallback(CheckedConsumer<String> callback);

    void RegisterExtractionProgressCallback(Consumer<ExtractorResult> callback);

    void ClearExtractionProgressCallbacks();
}
