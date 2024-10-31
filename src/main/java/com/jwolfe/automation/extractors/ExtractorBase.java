package com.jwolfe.automation.extractors;

import com.google.common.base.Stopwatch;
import com.jwolfe.automation.types.*;
import com.jwolfe.automation.types.records.ExtractionRecord;
import com.jwolfe.ankyl.commons.core.CheckedConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ExtractorBase implements Extractor {
    protected final Logger logger = LogManager.getLogger();

    private String extractorName;
    private String description;
    private String category;
    private String family;
    private ExtractorDefinition definition;

    private boolean interactionRequired;

    public Logger getLogger() {
        return logger;
    }

    private AutomationConfiguration config;
    private RunnerSettings runnerSettings;
    private List<CheckedConsumer<String>> userInteractionRequestCallbacks;
    private List<Consumer<ExtractorResult>> extractionProgressCallbacks;

    @Override
    public String getName() {
        return getDefinition().getName();
    }

    @Override
    public String getExtractorName() {
        return extractorName;
    }

    protected void setExtractorName(String extractorName) {
        this.extractorName = extractorName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    protected void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    @Override
    public ExtractorDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(ExtractorDefinition definition) {
        this.definition = definition;
    }

    @Override
    public boolean isInteractionRequired() {
        return interactionRequired;
    }

    public void setInteractionRequired(boolean interactionRequired) {
        this.interactionRequired = interactionRequired;
    }

    protected ExtractorBase(AutomationConfiguration config, ExtractorDefinition definition) {
        this.setConfig(config);
        this.definition = definition;
    }

    protected ExtractorBase(AutomationConfiguration config, ExtractorDefinition definition, String extractorName) {
        this(config, definition);
        this.extractorName = extractorName;
    }

    protected ExtractorBase(AutomationConfiguration config, ExtractorDefinition definition, String extractorName, String category) {
        this(config, definition, extractorName);
        this.category = category;
    }

    protected ExtractorBase(AutomationConfiguration config, ExtractorDefinition definition, String extractorName, String category, String description) {
        this(config, definition, extractorName, category);
        this.description = description;
    }

    protected ExtractorBase(AutomationConfiguration config, ExtractorDefinition definition, String extractorName, String category, boolean interactionRequired) {
        this(config, definition, extractorName, category);
        this.interactionRequired = interactionRequired;
    }

    protected ExtractorBase(AutomationConfiguration config, ExtractorDefinition definition, String extractorName, String category, String description, boolean interactionRequired) {
        this(config, definition, extractorName, category, interactionRequired);
        this.description = description;
    }

    protected ExtractorBase(AutomationConfiguration config, ExtractorDefinition definition, String extractorName, String category, String family, String description, boolean interactionRequired) {
        this(config, definition, extractorName, category, description, interactionRequired);
        this.family = family;
    }

    public List<ExtractionRecord> getRecords(WebDriver driver) throws InterruptedException, InterruptedIOException {
        return getRecords(driver, null);
    }

    public List<ExtractionRecord> getRecords(WebDriver driver, ExtractorResult result) throws InterruptedException, InterruptedIOException {
        return null;
    }

    public ExtractorResult run(WebDriver driver) {
        ExtractorResult result = new ExtractorResult(this);
        return run(driver, result);
    }

    public ExtractorResult run(WebDriver driver, ExtractorResult result) {
        Stopwatch watch = Stopwatch.createUnstarted();
        watch.start();

        try {
            result.setRunStatus(RunStatus.Running);
            raiseExtractionProgressChanged(result);

            var extractedRecords = this.getRecords(driver, result);

            if (extractedRecords != null) {
                result.getRecords().addAll(extractedRecords);

                if (result.getRunStatus() == RunStatus.Running) {
                    result.setRunStatus(RunStatus.Succeeded);
                }
            } else {
                result.setRunStatus(RunStatus.Undefined);
            }

            raiseExtractionProgressChanged(result);
        } catch (InterruptedException | InterruptedIOException iex) {
            result.setRunStatus(RunStatus.Cancelled);
            logger.error("Extraction run interrupted. Cancelling extraction.");
        } catch (Exception ex) {
            result.setRunStatus(RunStatus.Failed);
            raiseExtractionProgressChanged(result);
            logger.error(ex);
        }

        watch.stop();
        result.setTotalRunDuration(watch.elapsed());
        raiseExtractionProgressChanged(result);

        return result;
    }

    @Override
    public void registerUserInteractionRequestCallback(CheckedConsumer<String> callback) {
        if (getUserInteractionRequestCallbacks() == null) {
            setUserInteractionRequestCallbacks(new ArrayList<>());
        }

        getUserInteractionRequestCallbacks().add(callback);
    }

    @Override
    public void registerExtractionProgressCallback(Consumer<ExtractorResult> callback) {
        if (getExtractionProgressCallbacks() == null) {
            setExtractionProgressCallbacks(new ArrayList<>());
        }

        getExtractionProgressCallbacks().add(callback);
    }

    @Override
    public void clearExtractionProgressCallbacks() {
        if (getExtractionProgressCallbacks() == null) {
            return;
        }

        getExtractionProgressCallbacks().clear();
    }

    protected void raiseUserInteractionRequests(String message) throws InterruptedException {
        raiseUserInteractionRequests(message, null);
    }

    protected void raiseUserInteractionRequests(String message, ExtractorResult result) throws InterruptedException {
        Stopwatch watch = Stopwatch.createUnstarted();
        if (result != null) {
            watch.start();
        }

        if (getUserInteractionRequestCallbacks() == null) {
            return;
        }

        for (var callback : getUserInteractionRequestCallbacks()) {
            callback.accept(message);
        }

        if (result != null) {
            watch.stop();
            var previousDuration = result.getInterventionDuration();
            if (previousDuration == null) {
                result.setInterventionDuration(watch.elapsed());
            } else {
                result.setInterventionDuration(previousDuration.plus(watch.elapsed()));
            }
        }
    }

    protected void raiseExtractionProgressChanged(ExtractorResult result) {
        if (getExtractionProgressCallbacks() == null) {
            return;
        }

        for (var callback : getExtractionProgressCallbacks()) {
            callback.accept(result);
        }
    }

    protected void executeOptionalStep(Runnable r, ExtractorResult result, String failedMessage) {
        try {
            if (r != null) {
                r.run();
            }
        } catch (Exception ex) {
            if (failedMessage != null) {
                logger.warn(failedMessage);
            }

            logger.warn("Optional step failed. Marking as partial success. (Exception: " + ex.getMessage() + ")");
            result.setRunStatus(RunStatus.Partial);
        }
    }

    protected void executeLogoutStep(Runnable logoutRunnable, ExtractorResult result) {
        executeOptionalStep(logoutRunnable, result, "Logout failed");
    }

    public SiteConfiguration getSiteConfig() {
        return getDefinition().getSiteConfiguration();
    }

    public AutomationConfiguration getConfig() {
        return config;
    }

    public void setConfig(AutomationConfiguration config) {
        this.config = config;
    }

    public RunnerSettings getRunnerSettings() {
        return runnerSettings;
    }

    public void setRunnerSettings(RunnerSettings runnerSettings) {
        this.runnerSettings = runnerSettings;
    }

    public List<CheckedConsumer<String>> getUserInteractionRequestCallbacks() {
        return userInteractionRequestCallbacks;
    }

    public void setUserInteractionRequestCallbacks(List<CheckedConsumer<String>> userInteractionRequestCallbacks) {
        this.userInteractionRequestCallbacks = userInteractionRequestCallbacks;
    }

    public List<Consumer<ExtractorResult>> getExtractionProgressCallbacks() {
        return extractionProgressCallbacks;
    }

    public void setExtractionProgressCallbacks(List<Consumer<ExtractorResult>> extractionProgressCallbacks) {
        this.extractionProgressCallbacks = extractionProgressCallbacks;
    }
}
