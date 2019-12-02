package com.jwolfe.automation.types;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;

import java.util.*;
import java.util.concurrent.*;

public class BrowserDriverOptions {
    private String chromeDriverLocation;
    private String chromeUserDataDirectory;
    private boolean chromeStartMaximized;
    private boolean chromeDisableExtensions;
    private boolean chromeDisableNotifications;
    private boolean chromeDisablePopupBlocking;
    private boolean chromeNoSandbox;
    private boolean chromeW3CEnabled;
    private boolean chromeCredentialsServiceEnabled;
    private boolean chromePasswordManagerEnabled;
    private int chromeImplicitTimeout;

    private String firefoxDriverLocation;
    private boolean firefoxStartMaximized;
    private boolean firefoxDisableNotifications;


    public String getChromeDriverLocation() {
        return chromeDriverLocation;
    }

    public void setChromeDriverLocation(String chromeDriverLocation) {
        this.chromeDriverLocation = chromeDriverLocation;
    }

    public String getChromeUserDataDirectory() {
        return chromeUserDataDirectory;
    }

    public void setChromeUserDataDirectory(String chromeUserDataDirectory) {
        this.chromeUserDataDirectory = chromeUserDataDirectory;
    }

    public boolean isChromeStartMaximized() {
        return chromeStartMaximized;
    }

    public void setChromeStartMaximized(boolean chromeStartMaximized) {
        this.chromeStartMaximized = chromeStartMaximized;
    }

    public boolean isChromeDisableExtensions() {
        return chromeDisableExtensions;
    }

    public void setChromeDisableExtensions(boolean chromeDisableExtensions) {
        this.chromeDisableExtensions = chromeDisableExtensions;
    }

    public boolean isChromeDisableNotifications() {
        return chromeDisableNotifications;
    }

    public void setChromeDisableNotifications(boolean chromeDisableNotifications) {
        this.chromeDisableNotifications = chromeDisableNotifications;
    }

    public boolean isChromeDisablePopupBlocking() {
        return chromeDisablePopupBlocking;
    }

    public void setChromeDisablePopupBlocking(boolean chromeDisablePopupBlocking) {
        this.chromeDisablePopupBlocking = chromeDisablePopupBlocking;
    }

    public boolean isChromeNoSandbox() {
        return chromeNoSandbox;
    }

    public void setChromeNoSandbox(boolean chromeNoSandbox) {
        this.chromeNoSandbox = chromeNoSandbox;
    }

    public boolean isChromeW3CEnabled() {
        return chromeW3CEnabled;
    }

    public void setChromeW3CEnabled(boolean chromeW3CEnabled) {
        this.chromeW3CEnabled = chromeW3CEnabled;
    }

    public boolean isChromeCredentialsServiceEnabled() {
        return chromeCredentialsServiceEnabled;
    }

    public void setChromeCredentialsServiceEnabled(boolean chromeCredentialsServiceEnabled) {
        this.chromeCredentialsServiceEnabled = chromeCredentialsServiceEnabled;
    }

    public boolean isChromePasswordManagerEnabled() {
        return chromePasswordManagerEnabled;
    }

    public void setChromePasswordManagerEnabled(boolean chromePasswordManagerEnabled) {
        this.chromePasswordManagerEnabled = chromePasswordManagerEnabled;
    }

    public int getChromeImplicitTimeout() {
        return chromeImplicitTimeout;
    }

    public void setChromeImplicitTimeout(int chromeImplicitTimeout) {
        this.chromeImplicitTimeout = chromeImplicitTimeout;
    }

    public String getFirefoxDriverLocation() {
        return firefoxDriverLocation;
    }

    public void setFirefoxDriverLocation(String firefoxDriverLocation) {
        this.firefoxDriverLocation = firefoxDriverLocation;
    }

    public boolean isFirefoxStartMaximized() {
        return firefoxStartMaximized;
    }

    public void setFirefoxStartMaximized(boolean firefoxStartMaximized) {
        this.firefoxStartMaximized = firefoxStartMaximized;
    }

    public boolean isFirefoxDisableNotifications() {
        return firefoxDisableNotifications;
    }

    public void setFirefoxDisableNotifications(boolean firefoxDisableNotifications) {
        this.firefoxDisableNotifications = firefoxDisableNotifications;
    }

    public ChromeOptions getChromeOptions() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);

        if(isChromeCredentialsServiceEnabled()) {
            prefs.put("credentials_enable_service", true);
        }

        if(isChromePasswordManagerEnabled()) {
            prefs.put("profile.password_manager_enabled", true);
        }

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);

        if(getChromeUserDataDirectory() != null) {
            options.addArguments(getChromeUserDataDirectory());
        }

        if(isChromeW3CEnabled()) {
            options.setExperimentalOption("w3c", true);
        }

        if(isChromeNoSandbox()) {
            options.addArguments("--no-sandbox");
        }

        if(isChromeStartMaximized()) {
            options.addArguments("start-maximized");
        }

        if(isChromeDisableExtensions()) {
            options.addArguments("--disable-extensions");
        }

        if(isChromeDisableNotifications()) {
            options.addArguments("--disable-notifications");
        }

        if(isChromeDisableNotifications()) {
            options.addArguments("--disable-popup-blocking");
        }

        return options;
    }

    public FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();

        if(isFirefoxDisableNotifications()) {
            options.addPreference("dom.webnotifications.enabled", false);
        } else {
            options.addPreference("dom.webnotifications.enabled", true);
        }

        return options;
    }

    public WebDriver getFirefoxDriver() {
        if(getFirefoxDriverLocation() != null) {
            System.setProperty("webdriver.gecko.driver",
                    getFirefoxDriverLocation());
        }

        var options = getFirefoxOptions();
        WebDriver driver = new FirefoxDriver(options);

        if(isFirefoxStartMaximized()) {
            driver.manage().window().maximize();
        }

        return driver;
    }

    public WebDriver getChromeDriver() {
        if(getChromeDriverLocation() != null) {
            System.setProperty("webdriver.chrome.driver",
                    getChromeDriverLocation());
        }

        var options = getChromeOptions();
        WebDriver driver = new ChromeDriver(options);

        if(chromeStartMaximized) {
            driver.manage().window().maximize();
        }

        driver.manage().timeouts().implicitlyWait(getChromeImplicitTimeout(), TimeUnit.SECONDS);

        return driver;
    }

    public BrowserDriverOptions() {
        // loadDefaultOptions();
    }

    private void loadDefaultOptions() {
        loadDefaultChromeOptions();
        loadDefaultFirefoxOptions();
    }

    private void loadDefaultChromeOptions() {
        setChromeCredentialsServiceEnabled(false);
        setChromePasswordManagerEnabled(false);
        setChromeW3CEnabled(false);
        setChromeNoSandbox(true);
        setChromeStartMaximized(true);
        setChromeDisableExtensions(true);
        setChromeDisableNotifications(true);
        setChromeDisablePopupBlocking(true);
        setChromeImplicitTimeout(20);
    }

    private void loadDefaultFirefoxOptions() {
        setFirefoxStartMaximized(true);
        setFirefoxDisableNotifications(true);
    }
}
