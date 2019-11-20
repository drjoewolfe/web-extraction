package com.jwolfe.automation.configuration;

import com.jwolfe.automation.types.AutomationConfiguration;

public interface ConfigLoader {
    AutomationConfiguration getConfigurationFromFile(String configFilePath);
    AutomationConfiguration getConfigurationFromFile(String configFilePath, String password);

    AutomationConfiguration getConfigurationFromResource(String resourcePath);
    AutomationConfiguration getConfigurationFromResource(String resourcePath, String password);
}
