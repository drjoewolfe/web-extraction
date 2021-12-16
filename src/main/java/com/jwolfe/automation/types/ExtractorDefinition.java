package com.jwolfe.automation.types;

public class ExtractorDefinition {
    private String type;
    private SiteConfiguration siteConfiguration;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SiteConfiguration getSiteConfiguration() {
        return siteConfiguration;
    }

    public void setSiteConfiguration(SiteConfiguration siteConfiguration) {
        this.siteConfiguration = siteConfiguration;
    }
}
