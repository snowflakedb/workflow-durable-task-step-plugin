package io.jenkins.plugins;


import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationReader {
    private static final Logger LOGGER = Logger.getLogger(ConfigurationReader.class.getName());

    public boolean isMaskingEnabled() {
        LOGGER.log(Level.INFO, "Reading configuration to determine if masking is enabled");
        return true;
    }

    public boolean isMaskingEnabledForCredential(String id) {
        LOGGER.log(Level.INFO, "Reading configuration to determine if credential masking is enabled");
        return true;
    }
}