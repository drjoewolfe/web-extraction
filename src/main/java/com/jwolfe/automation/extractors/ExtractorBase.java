package com.jwolfe.automation.extractors;

import com.google.common.base.Stopwatch;
import com.jwolfe.automation.types.AutomationConfiguration;
import com.jwolfe.automation.types.ExtractorResult;
import com.jwolfe.automation.types.RunStatus;
import com.jwolfe.automation.types.SiteConfiguration;
import com.jwolfe.automation.types.records.ExtractionRecord;
import com.jwolfe.ankyl.commons.core.CheckedConsumer;
import com.jwolfe.automation.utilities.SeleniumUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public abstract class ExtractorBase implements Extractor {
    protected final Logger logger = LogManager.getLogger();

    private String name;
    private String description;
    private String category;
    private String family;

    private boolean interactionRequired;

    public Logger getLogger() {
        return logger;
    }

    protected AutomationConfiguration config;
    protected SiteConfiguration siteConfig;
    protected List<CheckedConsumer<String>> userInteractionRequestCallbacks;
    protected List<Consumer<ExtractorResult>> extractionProgressCallbacks;

    @Override
    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
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
    public boolean isInteractionRequired() {
        return interactionRequired;
    }

    public void setInteractionRequired(boolean interactionRequired) {
        this.interactionRequired = interactionRequired;
    }

    protected ExtractorBase(AutomationConfiguration config) {
        this.config = config;
    }

    protected ExtractorBase(AutomationConfiguration config, String name) {
        this(config);
        this.name = name;
    }

    protected ExtractorBase(AutomationConfiguration config, String name, String category) {
        this(config, name);
        this.category = category;
    }

    protected ExtractorBase(AutomationConfiguration config, String name, String category, String description) {
        this(config, name, category);
        this.description = description;
    }

    protected ExtractorBase(AutomationConfiguration config, String name, String category, boolean interactionRequired) {
        this(config, name, category);
        this.interactionRequired = interactionRequired;
    }

    protected ExtractorBase(AutomationConfiguration config, String name, String category, String description, boolean interactionRequired) {
        this(config, name, category, interactionRequired);
        this.description = description;
    }

    protected ExtractorBase(AutomationConfiguration config, String name, String category, String family, String description, boolean interactionRequired) {
        this(config, name, category, description, interactionRequired);
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
        if (userInteractionRequestCallbacks == null) {
            userInteractionRequestCallbacks = new ArrayList<>();
        }

        userInteractionRequestCallbacks.add(callback);
    }

    @Override
    public void registerExtractionProgressCallback(Consumer<ExtractorResult> callback) {
        if (extractionProgressCallbacks == null) {
            extractionProgressCallbacks = new ArrayList<>();
        }

        extractionProgressCallbacks.add(callback);
    }

    @Override
    public void clearExtractionProgressCallbacks() {
        if (extractionProgressCallbacks == null) {
            return;
        }

        extractionProgressCallbacks.clear();
    }

    protected void raiseUserInteractionRequests(String message) throws InterruptedException {
        raiseUserInteractionRequests(message, null);
    }

    protected void raiseUserInteractionRequests(String message, ExtractorResult result) throws InterruptedException {
        Stopwatch watch = Stopwatch.createUnstarted();
        if (result != null) {
            watch.start();
        }

        if (userInteractionRequestCallbacks == null) {
            return;
        }

        for (var callback : userInteractionRequestCallbacks) {
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
        if (extractionProgressCallbacks == null) {
            return;
        }

        for (var callback : extractionProgressCallbacks) {
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

            logger.warn("Optional step failed. Marking as partial success.");
            result.setRunStatus(RunStatus.Partial);
        }
    }

    protected void executeLogoutStep(Runnable logoutRunnable, ExtractorResult result) {
        executeOptionalStep(logoutRunnable, result, "Logout failed");
    }
}
