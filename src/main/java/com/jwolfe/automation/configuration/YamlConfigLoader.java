package com.jwolfe.automation.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jwolfe.ankyl.commons.utils.ResourceHelper;
import com.jwolfe.ankyl.crypto.AesBox;
import com.jwolfe.automation.types.AutomationConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class YamlConfigLoader implements ConfigLoader {
    @Override
    public AutomationConfiguration getConfigurationFromFile(String configFilePath) {
        return getConfigurationFromFile(configFilePath, null);
    }

//    @Override
//    public AutomationConfiguration GetConfiguration(String configFilePath, String password) {
//        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//        mapper.findAndRegisterModules();
//
//        var extension = FilenameUtils.getExtension(configFilePath);
//
//        AutomationConfiguration configuration = null;
//        if(extension.equals("enc")) {
//            // Encrypted configuration
//            var box = new AesBox();
//            var contents = box.getFileContentsAsDecryptedByteArray(configFilePath, password);
//            try {
//                configuration = mapper.readValue(contents, AutomationConfiguration.class);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        else {
//            // Plain text configuration
//            try {
//                configuration = mapper.readValue(new File(configFilePath), AutomationConfiguration.class);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return configuration;
//    }

    @Override
    public AutomationConfiguration getConfigurationFromFile(final String configFilePath, final String password) {
        var extension = FilenameUtils.getExtension(configFilePath);

        AutomationConfiguration configuration = null;
        if (extension.equals("enc")) {
            // Encrypted configuration
            var box = new AesBox();
            var contents = box.getFileContentsAsDecryptedByteArray(configFilePath, password);
            var stream = new ByteArrayInputStream(contents);
            configuration = getConfigurationFromStream(stream);
        } else {
            try {
                var stream = new FileInputStream(configFilePath);
                configuration = getConfigurationFromStream(stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return configuration;
    }

    @Override
    public AutomationConfiguration getConfigurationFromResource(final String resourcePath) {
        var stream = ResourceHelper.getResourceAsStream(resourcePath);
        return getConfigurationFromStream(stream);
    }

    @Override
    public AutomationConfiguration getConfigurationFromResource(final String resourcePath, final String password) {
        AutomationConfiguration configuration = null;
        if (password == null || password.trim().equals("")) {
            configuration = getConfigurationFromResource(resourcePath);
        } else {
            // Encrypted configuration
            var box = new AesBox();
            try {
                var stream = ResourceHelper.getResourceAsStream(resourcePath);
                byte[] encryptedByteArray = IOUtils.toByteArray(stream);
                var decryptedByteArray = box.decryptAsByteArray(encryptedByteArray, password);
                var decryptedStream = new ByteArrayInputStream(decryptedByteArray);
                configuration = getConfigurationFromStream(decryptedStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return configuration;
    }

    private AutomationConfiguration getConfigurationFromStream(final InputStream stream) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();

        AutomationConfiguration configuration = null;
        try {
            configuration = mapper.readValue(stream, AutomationConfiguration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return configuration;
    }
}
