package com.jwolfe.automation.extractors;

import com.jwolfe.automation.types.AutomationConfiguration;
import com.jwolfe.automation.types.ExtractorDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ExtractorFactory {
    public static ExtractorFactory factoryInstance;

//    private Map<String, Extractor> extractorInstances;
    private Map<ExtractorDefinition, Extractor> extractorInstances;

    private ExtractorFactory() {
        extractorInstances = new HashMap<>();
    }

    public static ExtractorFactory getInstance() {
        if (factoryInstance == null) {
            factoryInstance = new ExtractorFactory();
        }

        return factoryInstance;
    }

//    public Extractor getExtractor(String name, AutomationConfiguration configuration) {
//        Extractor instance = null;
//
//        if (extractorInstances.containsKey(name)) {
//            return extractorInstances.get(name);
//        }
//
//        Class<?> cls = null;
//        try {
//            cls = Class.forName(name);
//            Constructor<?> constructor = cls.getConstructor(AutomationConfiguration.class);
//            instance = (Extractor) constructor.newInstance(configuration);
//
//        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        extractorInstances.put(name, instance);
//        return instance;
//    }

    public Extractor getExtractor(ExtractorDefinition definition, AutomationConfiguration configuration) {
        Extractor instance = null;

        if (extractorInstances.containsKey(definition)) {
            return extractorInstances.get(definition);
        }

        Class<?> cls = null;
        try {
            String type = definition.getType();
            cls = Class.forName(type);
            Constructor<?> constructor = cls.getConstructor(AutomationConfiguration.class, ExtractorDefinition.class);
            instance = (Extractor) constructor.newInstance(configuration, definition);

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        extractorInstances.put(definition, instance);
        return instance;
    }
}
