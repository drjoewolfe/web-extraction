package com.jwolfe.automation.utilities;

import com.jwolfe.automation.types.SiteConfiguration;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class SeleniumUtilities {
    public static void Login(WebDriver driver, SiteConfiguration siteConfig, String userNameInputId, String passwordInputId, String loginButtonId, Logger logger) throws InterruptedException {
        Login(driver, siteConfig, userNameInputId, passwordInputId, loginButtonId, 0, logger);
    }

    public static void Login(WebDriver driver, SiteConfiguration siteConfig, String userNameInputId, String passwordInputId, String loginButtonId, int loginWaitSeconds, Logger logger) throws InterruptedException {
        logger.info("Navigating to login page");
        driver.get(siteConfig.getLoginUrl());

        if(loginWaitSeconds > 0) {
            Sleep(loginWaitSeconds, logger);
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

    public static void WaitTillNavigatedToUrlSuffix(WebDriver driver, String urlSuffix, Logger logger) {
        logger.info("Waiting for url suffix - " + urlSuffix);
        String lowerUrlSuffix = urlSuffix.toLowerCase();

        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().toLowerCase().endsWith(lowerUrlSuffix);
            }
        });
    }

    public static void WaitTillElementPresent(WebDriver driver, String id) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }

    public static void WaitTillXPathElementPresent(WebDriver driver, String elementXPath, int timeout) {
        (new WebDriverWait(driver, timeout)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementXPath)));
    }

    public static void WaitTillXPathElementPresent(WebDriver driver, String elementXPath) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(elementXPath)));
    }

    public static void WaitTillElementVisible(WebDriver driver, String id) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
    }

    public static void WaitTillElementByNameVisible(WebDriver driver, String name) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));
    }

    public static void WaitTillElementByClassVisible(WebDriver driver, String className) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.className(className)));
    }

    public static void WaitTillXPathElementVisible(WebDriver driver, String elementXPath, int timeout) {
        (new WebDriverWait(driver, timeout)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementXPath)));
    }

    public static void WaitTillXPathElementVisible(WebDriver driver, String elementXPath) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementXPath)));
    }

    public static void ScrollElementByXPathToView(WebDriver driver, String elementXPath) {
        var element = driver.findElement(By.xpath(elementXPath));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void WaitTillXPathElementClickable(WebDriver driver, String elementXPath) {
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath(elementXPath)));
    }

    public static void WaitAndClickXPathElement(WebDriver driver, String elementXPath) {
        WaitAndClickXPathElement(driver, elementXPath, null);
    }

    public static void WaitAndClickXPathElement(WebDriver driver, String elementXPath, Function<? super WebDriver, Boolean> additionalWaitPredicate) {
        WaitTillXPathElementClickable(driver, elementXPath);
        if(additionalWaitPredicate != null) {
            (new WebDriverWait(driver, 10)).until(additionalWaitPredicate);
        }

        WebElement clickableElement = driver.findElement(By.xpath(elementXPath));
        clickableElement.click();
    }

    public static void ClickButtonByIdIfExists(WebDriver driver, String buttonId, Logger logger) {
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

    public static void ClickElementByXPathIfExists(WebDriver driver, String xpath, Logger logger) {
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

    public static void ClickButtonByIdIfExists(WebDriver driver, String iframeId, String buttonId, Logger logger) {
        boolean clicked = false;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
            var frames = driver.findElements(By.id(iframeId));
            if(frames.size() != 0) {
                driver.switchTo().frame(frames.get(0));

                var buttons = driver.findElements(By.id(buttonId));
                if(buttons.size() != 0) {
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

        if(!clicked) {
            logger.info("Could not find button to click - " + buttonId + ". Proceeding");
        }
    }

    public static void Sleep(int seconds, Logger logger) throws InterruptedException {
        logger.info("Sleeping for " + seconds + " seconds");
        Thread.sleep(seconds * 1000);

        logger.info("Resuming from sleep");
    }

    public static String GetString(WebElement rootElement, String childXPath) {
        return rootElement.findElement(By.xpath(childXPath)).getText();
    }

    public static double GetDouble(WebElement element) {
        return Double.parseDouble( element.getText().replaceAll("[^0-9\\.]", ""));
    }

    public static double GetDouble(WebElement element, String[] preliminaryTrimStrings) {
        String text = element.getText();
        for(String str : preliminaryTrimStrings) {
            text = text.replaceAll(str, "");
        }

        return Double.parseDouble( text.replaceAll("[^0-9\\.]", ""));
    }

    public static double GetDoubleForRupees(WebElement element) {
        return Double.parseDouble( element.getText().replace("Rs.", "").replaceAll("[^0-9\\.]", ""));
    }

    public static double GetDouble(WebElement rootElement, String childXPath) {
        return Double.parseDouble( rootElement.findElement(By.xpath(childXPath)).getText().replaceAll("[^0-9\\.]", ""));
    }

    public static double GetDouble(String amountString) {
        return Double.parseDouble( amountString.replaceAll("[^0-9\\.]", ""));
    }

    public static boolean ElementExists(WebDriver driver, String xPath) {
        return driver.findElements(By.xpath(xPath)).size() != 0;
    }

    public static boolean ElementExistsByClassName(WebDriver driver, String className) {
        return driver.findElements(By.className(className)).size() != 0;
    }

    public static boolean ElementExistsByClassName(WebElement element, String className) {
        return element.findElements(By.className(className)).size() != 0;
    }

    public static String GetElementValueByClassName(WebElement element, String className, String defaultValue) {
        var elements = element.findElements(By.className(className));
        if(elements.size() == 0) {
            return defaultValue;
        }

        return elements.get(0).getText();
    }
}
