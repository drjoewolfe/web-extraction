package com.jwolfe.automation.types;

import com.jwolfe.automation.extractors.Extractor;

import java.util.List;

public class AutomationConfiguration {
    private List<SiteConfiguration> siteConfigurations;

    private List<String> extractorNames;

    private String outputFilePath;

    private boolean appendToFile;

    private boolean backupExistingFile;

    private String browser;

    private BrowserDriverOptions browserDriverOptions;

    private boolean logoutAfterExtraction;

    private boolean closeBrowserAfterExtraction;

    private boolean confirmOnUserInteractions;

    private RunnerSettings runnerSettings;

    public List<SiteConfiguration> getSiteConfigurations() {
        return siteConfigurations;
    }

    public void setSiteConfigurations(List<SiteConfiguration> siteConfigurations) {
        this.siteConfigurations = siteConfigurations;
    }

    public List<String> getExtractorNames() {
        return extractorNames;
    }

    public void setExtractorNames(final List<String> extractorNames) {
        this.extractorNames = extractorNames;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(final String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public boolean isAppendToFile() {
        return appendToFile;
    }

    public void setAppendToFile(final boolean appendToFile) {
        this.appendToFile = appendToFile;
    }

    public boolean isBackupExistingFile() {
        return backupExistingFile;
    }

    public void setBackupExistingFile(final boolean backupExistingFile) {
        this.backupExistingFile = backupExistingFile;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(final String browser) {
        this.browser = browser;
    }

    public BrowserDriverOptions getBrowserDriverOptions() {
        return browserDriverOptions;
    }

    public void setBrowserDriverOptions(BrowserDriverOptions browserDriverOptions) {
        this.browserDriverOptions = browserDriverOptions;
    }

    public boolean getLogoutAfterExtraction() {
        return logoutAfterExtraction;
    }

    public void setLogoutAfterExtraction(final boolean logoutAfterExtraction) {
        this.logoutAfterExtraction = logoutAfterExtraction;
    }

    public boolean getCloseBrowserAfterExtraction() {
        return closeBrowserAfterExtraction;
    }

    public void setCloseBrowserAfterExtraction(final boolean closeBrowserAfterExtraction) {
        this.closeBrowserAfterExtraction = closeBrowserAfterExtraction;
    }

    public boolean isConfirmOnUserInteractions() {
        return confirmOnUserInteractions;
    }

    public void setConfirmOnUserInteractions(boolean confirmOnUserInteractions) {
        this.confirmOnUserInteractions = confirmOnUserInteractions;
    }

    public RunnerSettings getRunnerSettings() {
        return runnerSettings;
    }

    public void setRunnerSettings(RunnerSettings runnerSettings) {
        this.runnerSettings = runnerSettings;
    }

    public AutomationConfiguration() {
        this.browserDriverOptions = new BrowserDriverOptions();
    }

    public SiteConfiguration getSiteConfiguration(final String name) {
        return this.siteConfigurations.stream()
                .filter(config -> name.equals(config.getName()))
                .findAny()
                .orElse(null);
    }
}
