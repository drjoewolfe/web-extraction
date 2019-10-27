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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartingUrl() {
        return startingUrl;
    }

    public void setStartingUrl(String startingUrl) {
        this.startingUrl = startingUrl;
    }

    public String getStartingContent() {
        return startingContent;
    }

    public void setStartingContent(String startingContent) {
        this.startingContent = startingContent;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SiteConfiguration() {
    }
}
