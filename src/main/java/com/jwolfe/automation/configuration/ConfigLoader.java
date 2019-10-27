package com.jwolfe.automation.configuration;

import com.jwolfe.automation.types.AutomationConfiguration;

import java.io.InputStream;

public interface ConfigLoader {
    AutomationConfiguration GetConfigurationFromFile(String configFilePath);
    AutomationConfiguration GetConfigurationFromFile(String configFilePath, String password);

    AutomationConfiguration GetConfigurationFromResource(String resourcePath);
    AutomationConfiguration GetConfigurationFromResource(String resourcePath, String password);
}
