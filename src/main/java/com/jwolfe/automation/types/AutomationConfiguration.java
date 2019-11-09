package com.jwolfe.automation.types;

import java.util.List;

public class AutomationConfiguration {
    private List<SiteConfiguration> siteConfigurations;

    private List<String> extractorNames;

    private String outputFilePath;

    private boolean appendToFile;

    private boolean backupExistingFile;

    private String browser;

    private Boolean logoutAfterExtraction;

    private Boolean closeBrowserAfterExtraction;

    public List<SiteConfiguration> getSiteConfigurations() {
        return siteConfigurations;
    }

    public void setSiteConfigurations(List<SiteConfiguration> siteConfigurations) {
        this.siteConfigurations = siteConfigurations;
    }

    public List<String> getExtractorNames() {
        return extractorNames;
    }

    public void setExtractorNames(List<String> extractorNames) {
        this.extractorNames = extractorNames;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public boolean isAppendToFile() {
        return appendToFile;
    }

    public void setAppendToFile(boolean appendToFile) {
        this.appendToFile = appendToFile;
    }

    public boolean isBackupExistingFile() {
        return backupExistingFile;
    }

    public void setBackupExistingFile(boolean backupExistingFile) {
        this.backupExistingFile = backupExistingFile;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public Boolean getLogoutAfterExtraction() {
        return logoutAfterExtraction;
    }

    public void setLogoutAfterExtraction(Boolean logoutAfterExtraction) {
        this.logoutAfterExtraction = logoutAfterExtraction;
    }

    public Boolean getCloseBrowserAfterExtraction() {
        return closeBrowserAfterExtraction;
    }

    public void setCloseBrowserAfterExtraction(Boolean closeBrowserAfterExtraction) {
        this.closeBrowserAfterExtraction = closeBrowserAfterExtraction;
    }

    public AutomationConfiguration() {

    }

    public SiteConfiguration getSiteConfiguration(String name) {
        return this.siteConfigurations.stream()
                .filter(config -> name.equals(config.getName()))
                .findAny()
                .orElse(null);
    }
}
