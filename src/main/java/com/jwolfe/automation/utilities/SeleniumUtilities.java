package com.jwolfe.automation.utilities;

import com.jwolfe.automation.types.SiteConfiguration;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public final class SeleniumUtilities {
    private SeleniumUtilities() {

    }

    public static void login(final WebDriver driver, final SiteConfiguration siteConfig, final String userNameInputId, final String passwordInputId, final String loginButtonId, final Logger logger) throws InterruptedException {
        login(driver, siteConfig, userNameInputId, passwordInputId, loginButtonId, 0, logger);
    }

    public static void login(final WebDriver driver, final SiteConfiguration siteConfig, final String userNameInputId, final String passwordInputId, final String loginButtonId, final int loginWaitSeconds, final Logger logger) throws InterruptedException {
        logger.info("Navigating to login page");
        driver.get(siteConfig.getLoginUrl());

        if (loginWaitSeconds > 0) {
            sleep(loginWaitSeconds, logger);
        }

        logger.info("Logging in");
        WebElement userIdBox = driver.findElement(By.id(userNameInputId));
        WebElement passwordBox = driver.findElement(By.id(passwordInputId));
        WebElement loginButton = driver.findElement(By.id(loginButtonId));

        userIdBox.sendKeys(siteConfig.getUserName());
        passwordBox.sendKeys(siteConfig.getPassword());

        loginButton.click();
        logger.info("<Hope> Should be logged in now");
    }

    public static void waitTillNavigatedToUrlSuffix(final WebDriver driver, final String urlSuffix, final Logger logger) {
        logger.info("Waiting for url suffix - " + urlSuffix);
        String lowerUrlSuffix = urlSuffix.toLowerCase();

        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().toLowerCase().endsWith(lowerUrlSuffix);
            }
        });
    }

    public static void waitTillNavigatedToUrlPathSuffix(final WebDriver driver, final String pathSuffix, final Logger logger) {
        logger.info("Waiting for url path suffix - " + pathSuffix);
        String lowerPathSuffix = pathSuffix.toLowerCase();

        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                try {
                    var url = new URL(d.getCurrentUrl());
                    return url.getPath().toLowerCase().endsWith(lowerPathSuffix);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
    }

    public static void waitTillElementPresent(final WebDriver driver, final String id) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }

    public static void waitTillXPathElementPresent(final WebDriver driver, final String elementXPath, final int timeout) {
        (new WebDriverWait(driver, timeout)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementXPath)));
    }

    public static void waitTillXPathElementPresent(final WebDriver driver, final String elementXPath) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementXPath)));
    }

    public static void waitTillElementVisible(final WebDriver driver, final String id) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
    }

    public static void waitTillElementByNameVisible(final WebDriver driver, final String name) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));
    }

    public static void waitTillElementByClassVisible(final WebDriver driver, final String className) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.className(className)));
    }

    public static void waitTillXPathElementVisible(final WebDriver driver, final String elementXPath, final int timeout) {
        (new WebDriverWait(driver, timeout)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementXPath)));
    }

    public static void waitTillXPathElementVisible(final WebDriver driver, final String elementXPath) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementXPath)));
    }

    public static void scrollElementByXPathToView(final WebDriver driver, final String elementXPath) {
        var element = driver.findElement(By.xpath(elementXPath));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void waitTillXPathElementClickable(final WebDriver driver, final String elementXPath) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(elementXPath)));
    }

    public static void waitAndClickXPathElement(final WebDriver driver, final String elementXPath) {
        waitAndClickXPathElement(driver, elementXPath, null);
    }

    public static void waitAndClickXPathElement(final WebDriver driver, final String elementXPath, final Function<? super WebDriver, Boolean> additionalWaitPredicate) {
        waitTillXPathElementClickable(driver, elementXPath);
        if (additionalWaitPredicate != null) {
            (new WebDriverWait(driver, 10)).until(additionalWaitPredicate);
        }

        WebElement clickableElement = driver.findElement(By.xpath(elementXPath));
        clickableElement.click();
    }

    public static boolean elementByIdExists(final WebDriver driver, final String id, final Logger logger) {
        boolean exists = false;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);

            var buttons = driver.findElements(By.id(id));
            if (buttons.size() != 0) {
                exists = true;
            }
        } catch (Exception ex) {
            exists = false;
        } finally {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }

        if (exists) {
            logger.info("Element by id - " + id + " exists");
        } else {
            logger.info("Element by id - " + id + " does not exist");
        }

        return exists;
    }

    public static boolean elementByClassNameExists(final WebDriver driver, final String className, final Logger logger) {
        boolean exists = false;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);

            var elements = driver.findElements(By.className(className));
            if (elements.size() != 0) {
                exists = true;
            }
        } catch (Exception ex) {
            exists = false;
        } finally {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }

        if (exists) {
            logger.info("Element by class - " + className + " exists");
        } else {
            logger.info("Element by class - " + className + " does not exist");
        }

        return exists;
    }

    public static boolean elementByClassNameExists(final WebElement element, final String className) {
        return element.findElements(By.className(className)).size() != 0;
    }

    public static void clickButtonByIdIfExists(final WebDriver driver, final String buttonId, final Logger logger) {
        boolean clicked = false;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);

            var buttons = driver.findElements(By.id(buttonId));
            if (buttons.size() != 0) {
                buttons.get(0).click();
                clicked = true;
            }
        } catch (Exception ex) {
            clicked = false;
        } finally {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }

        if (!clicked) {
            logger.info("Could not find button to click - " + buttonId + ". Proceeding");
        }
    }

    public static void clickElementByXPathIfExists(final WebDriver driver, final String xpath, final Logger logger) {
        boolean clicked = false;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);

            var elements = driver.findElements(By.xpath(xpath));
            if (elements.size() != 0) {
                elements.get(0).click();
                clicked = true;
            }
        } catch (Exception ex) {
            clicked = false;
        } finally {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }

        if (!clicked) {
            logger.info("Could not find element to click - " + xpath + ". Proceeding");
        }
    }

    public static void clickButtonByIdIfExists(final WebDriver driver, final String iframeId, final String buttonId, final Logger logger) {
        boolean clicked = false;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
            var frames = driver.findElements(By.id(iframeId));
            if (frames.size() != 0) {
                driver.switchTo().frame(frames.get(0));

                var buttons = driver.findElements(By.id(buttonId));
                if (buttons.size() != 0) {
                    buttons.get(0).click();
                    clicked = true;
                }
            }
        } catch (Exception ex) {
            clicked = false;
        } finally {
            driver.switchTo().defaultContent();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }

        if (!clicked) {
            logger.info("Could not find button to click - " + buttonId + ". Proceeding");
        }
    }

    public static void sleep(final int seconds, final Logger logger) throws InterruptedException {
        logger.info("Sleeping for " + seconds + " seconds");
        Thread.sleep(seconds * 1000);

        logger.info("Resuming from sleep");
    }

    public static void sleepNoException(final int seconds, final Logger logger) {
        logger.info("Sleeping for " + seconds + " seconds");
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("Resuming from sleep");
    }


    public static String getString(final WebElement rootElement, final String childXPath) {
        return rootElement.findElement(By.xpath(childXPath)).getText();
    }

    public static double getDouble(final WebElement element) {
        return Double.parseDouble(element.getText().replaceAll("[^0-9\\.]", ""));
    }

    public static double getDouble(final WebElement element, final String[] preliminaryTrimStrings) {
        String text = element.getText();
        for (String str : preliminaryTrimStrings) {
            text = text.replaceAll(str, "");
        }

        return Double.parseDouble(text.replaceAll("[^0-9\\.]", ""));
    }

    public static double getDoubleForRupees(final WebElement element) {
        return Double.parseDouble(element.getText().replace("Rs.", "").replaceAll("[^0-9\\.]", ""));
    }

    public static double getDouble(final WebElement rootElement, final String childXPath) {
        return Double.parseDouble(rootElement.findElement(By.xpath(childXPath)).getText().replaceAll("[^0-9\\.]", ""));
    }

    public static double getDouble(final String amountString) {
        if(amountString.trim().equals("--")) {
            return 0;
        }

        return Double.parseDouble(amountString.replaceAll("[^-0-9\\.]", ""));
    }

    public static boolean elementExists(final WebDriver driver, final String xPath) {
        return driver.findElements(By.xpath(xPath)).size() != 0;
    }

    public static String getElementValueByClassName(final WebElement element, final String className, final String defaultValue) {
        var elements = element.findElements(By.className(className));
        if (elements.size() == 0) {
            return defaultValue;
        }

        return elements.get(0).getText();
    }

    public static void acceptAlertIfPresent(final WebDriver driver, final Logger logger) {
        try {
            var alert = driver.switchTo().alert();
            alert.accept();
            logger.info("Alert accepted");

        } catch (NoAlertPresentException Ex) {
            logger.info("No alert found");
        }
    }
}