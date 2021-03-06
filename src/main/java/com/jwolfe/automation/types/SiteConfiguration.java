package com.jwolfe.automation.types;

public class SiteConfiguration {
    private String name;
    private String startingUrl;
    private String startingContent;
    private String loginUrl;
    private String portfolioUrl;
    private String userName;
    private String encryptedPassword;
    private String password;
    private String pan;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getStartingUrl() {
        return startingUrl;
    }

    public void setStartingUrl(final String startingUrl) {
        this.startingUrl = startingUrl;
    }

    public String getStartingContent() {
        return startingContent;
    }

    public void setStartingContent(final String startingContent) {
        this.startingContent = startingContent;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(final String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(final String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(final String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(final String pan) {
        this.pan = pan;
    }

    public SiteConfiguration() {
    }
}
