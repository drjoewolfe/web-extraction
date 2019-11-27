package com.jwolfe.automation;

import com.jwolfe.automation.exporters.JsonExporter;
import com.jwolfe.automation.extractors.Extractor;
import com.jwolfe.automation.extractors.ExtractorFactory;
import com.jwolfe.automation.types.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ExtractorAutomation {
    private static final Logger logger = LogManager.getLogger("ExtractorAutomation");
    private List<RunSummaryUpdatedListener> runSummmaryUpdatedlisteners = new ArrayList<RunSummaryUpdatedListener>();

    private static WebDriver driver;

    public ExtractionRunSummary Run(final AutomationConfiguration configuration) throws InterruptedException {
        logger.info("Initiating extraction run");
        ExtractionRunSummary runSummary = new ExtractionRunSummary();
        initializeRunSummaryReport(configuration, runSummary);

        var extract = runSummary.getExtract();
        var today = Calendar.getInstance().getTime();
        extract.setExtractDate(today);
        extract.setVersion(1);
        notifyRunSummaryUpdatedListeners(runSummary);

        extractRecords(configuration, runSummary);
        logger.info("Record extraction completed");

        logger.info("Exporting extracted records");
        new JsonExporter().export(extract, configuration.getOutputFilePath(), configuration.isAppendToFile(), configuration.isBackupExistingFile());
        notifyRunSummaryUpdatedListeners(runSummary);

        logger.info("Record export completed");
        logger.info("Extraction took " + runSummary.getTotalExtractionTimeInSeconds() + " seconds");
        for (var result : runSummary.getExtractorResults()) {
            logger.info("Extractor: " + result.getExtractor().getName()
                    + "; Status: " + result.getRunStatus()
                    + "; # Records: " + result.getRecords().size()
                    + "; Total Duration: " + result.getTotalRunDuration().toSeconds() + " seconds"
                    + "; Automated Time: " + (result.getAutomatedRunDuration() != null ? result.getAutomatedRunDuration().toSeconds() + " seconds" : "-")
                    + "; Intervention Time: " + (result.getInterventionDuration() != null ? result.getInterventionDuration().toSeconds() + " seconds" : "-"));
        }

        return runSummary;
    }

    private void initializeRunSummaryReport(final AutomationConfiguration configuration, final ExtractionRunSummary runSummary) {
        var extractorFactory = ExtractorFactory.getInstance();

        var extractorsToAttempt = runSummary.getExtractorsToAttempt();
        var extractorResultMap = runSummary.getExtractorResultMap();
        extractorResultMap.clear();

        for (String name : configuration.getExtractorNames()) {
            var extractor = extractorFactory.getExtractor(name, configuration);

            ExtractorResult extractorResult = new ExtractorResult(extractor);
            extractorResult.setRunStatus(RunStatus.Queued);
            extractorResultMap.put(name, extractorResult);
            extractorsToAttempt.add(extractor);
        }
    }

    public void addRunSummaryUpdatedListener(final RunSummaryUpdatedListener listener) {
        if (listener == null) {
            return;
        }

        runSummmaryUpdatedlisteners.add(listener);
    }

    private void notifyRunSummaryUpdatedListeners(final ExtractionRunSummary summary) {
        for (RunSummaryUpdatedListener listener : runSummmaryUpdatedlisteners) {
            listener.runSummaryUpdated(summary);
        }
    }

    private void extractRecords(final AutomationConfiguration config, final ExtractionRunSummary runSummary) throws InterruptedException {
        var records = runSummary.getExtract().getRecords();

        logger.info("Launching browser");
        WebDriver driver = initializeDriver(config);

        var extractorFactory = ExtractorFactory.getInstance();
        Extractor extractor = null;

        boolean threadCancelled = false;
        logger.info("Running extractors");
        for (String name : config.getExtractorNames()) {
            var extractorResult = runSummary.getExtractorResultMap().get(name);

            extractor = extractorFactory.getExtractor(name, config);
            extractor.clearExtractionProgressCallbacks();
            extractor.registerExtractionProgressCallback(new Consumer<ExtractorResult>() {
                @Override
                public void accept(ExtractorResult result) {
                    notifyRunSummaryUpdatedListeners(runSummary);
                }
            });

            extractor.run(driver, extractorResult);

            if (extractorResult.getRunStatus() == RunStatus.Succeeded
                    || extractorResult.getRunStatus() == RunStatus.Partial) {
                records.addAll(extractorResult.getRecords());
            } else if (extractorResult.getRunStatus() == RunStatus.Failed) {
                logger.error("Encountered error while extracting " + extractor.getName() + ". Skipping & continuing.");
            } else if (extractorResult.getRunStatus() == RunStatus.Cancelled) {
                logger.error("Extraction run interrupted. Cancelling extraction.");
                threadCancelled = true;
                break;
            }

            var durationInSeconds = extractorResult.getTotalRunDuration().toSeconds();
            logger.info("Extractor " + extractor.getName() + " took " + durationInSeconds + " seconds");
        }

        if (config.getCloseBrowserAfterExtraction()) {
            releaseDriver();
        }

        logger.info("Extraction complete. Total attempted: "
                + runSummary.getCountOfExtractorsAttempted()
                + ", Succeeded: "
                + runSummary.getCountOfExtractorsSucceeded()
                + ", Failed: "
                + runSummary.getCountOfExtractorsFailed());

        var countOfFailedExtractors = runSummary.getCountOfExtractorsFailed();
        if (countOfFailedExtractors > 0) {
            List<String> failedExtractorNames = runSummary.getExtractorResults().stream()
                    .filter(result -> result.getRunStatus() == RunStatus.Failed)
                    .map(result -> result.getExtractor().getName())
                    .collect(Collectors.toList());
            logger.info("Failed extractors: " + String.join(", ", failedExtractorNames));
        }

        if (threadCancelled) {
            throw new InterruptedException();
        }
    }

    private WebDriver initializeDriver(final AutomationConfiguration config) {
        boolean chromeRequested = config.getBrowser().equals("chrome");

        if (driver != null && !isBrowserClosed(driver)) {
            if ((chromeRequested && (driver instanceof ChromeDriver))
                    || (!chromeRequested && !(driver instanceof ChromeDriver))) {
                return driver;
            }
        }

        if (chromeRequested) {
            driver = initializeChromeDriver();
        } else {
            driver = initializeFirefoxDriver();
        }

        return driver;
    }

    private WebDriver initializeFirefoxDriver() {
        System.setProperty("webdriver.gecko.driver",
                "/home/apput/Desktop/dev/jw/jars/geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("dom.webnotifications.enabled", false);
        WebDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();

        return driver;
    }

    private WebDriver initializeChromeDriver() {
        System.setProperty("webdriver.chrome.driver",
                "/home/apput/Desktop/dev/jw/jars/chromedriver");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
//        prefs.put("credentials_enable_service", true);
//        prefs.put("profile.password_manager_enabled", true);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=/home/apput/.config/google-chrome/");
        options.setExperimentalOption("prefs", prefs);
//        options.setExperimentalOption("w3c", true);
//        options.addArguments("--no-sandbox");
        options.addArguments("start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-notifications");
        // options.addArguments("--disable-popup-blocking");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        return driver;
    }

    private void releaseDriver() {
        if (driver == null) {
            return;
        }

        driver.quit();
        driver = null;
    }

    private boolean isBrowserClosed(final WebDriver driver) {
        boolean isClosed = false;
        try {
            driver.getTitle();
        } catch (WebDriverException wde) {
            isClosed = true;
        }

        return isClosed;
    }
}
