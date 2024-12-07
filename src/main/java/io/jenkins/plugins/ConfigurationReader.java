package io.jenkins.plugins;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Read credential masking configurations
 * TODO: update this code to read configs from JCASC
 */
public class ConfigurationReader {
    private static final Logger LOGGER = Logger.getLogger(ConfigurationReader.class.getName());

    /**
     * Overall config to enable/disable credential masking.
     *
     * This is to be used during initial rollout to disable
     * globally or on a specific controller if we detect
     * any side effects to the masking logic.
     *
     * TODO: update this code to read configs from JCASC
     * @return true if masking is enabled, false otherwise
     */
    public boolean isMaskingEnabled() {
        LOGGER.log(Level.FINE, "Reading configuration to determine if masking is enabled");
        return true;
    }

    /**
     * Config to enable/disable credential masking for a
     * sepcific credential (by credential id).
     *
     * This is to be used during initial rollout to build up the list
     * of creds we want to mask.
     *
     * TODO: update this code to read configs from JCASC
     * @param id credential masking id
     * @return true if this credential is to be masked, false otherwise.
     */
    public boolean isMaskingEnabledForCredential(String id) {
        LOGGER.log(Level.FINE, "Reading configuration to determine if credential masking is enabled");
        return true;
    }
}
