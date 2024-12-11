package com.snowflake.jenkins.workflow_durable_task_step;

import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jenkins.model.Jenkins;

import com.cloudbees.plugins.credentials.domains.*;
import com.cloudbees.plugins.credentials.CredentialsStore;
import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.SystemCredentialsProvider;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import com.cloudbees.jenkins.plugins.awscredentials.AWSCredentialsImpl;

/**
 * Factory class for credential masking
 *
 */
public class MaskFactory {

    private static final Logger LOGGER = Logger.getLogger(MaskFactory.class.getName());

    /**
     * Get an OutputStream with the logic to mask credentials.
     */
    public static OutputStream getMaskedStream(OutputStream delegate) {
        ConfigurationReader configurationReader = new ConfigurationReader();
        return getMaskedStream(delegate, configurationReader);
    }

    /**
     * Helper method that takes in a configurator class.
     * Use by testing log that mocks configurator.
     */
    public static OutputStream getMaskedStream(OutputStream delegate, ConfigurationReader configurationReader) {
        boolean enableCredentialsMasking = configurationReader.isMaskingEnabled();
        LOGGER.log(Level.INFO, "Creating MaskingOutputStream " + enableCredentialsMasking);
        if (enableCredentialsMasking == true) {
            LOGGER.log(Level.FINE, "Credential masking enabled, creating MaskingOutputStream");
            List<String> valuesToMask = searchForValuesToMask(configurationReader);
            return new MaskingOutputStream(delegate, valuesToMask);
        }
        else {
            LOGGER.log(Level.FINE, "Credential masking disabled, returning original OutputStream");
            return delegate;
        }
    }

    /**
     * Scan Jenkins credential store for credentials to mask.
     *
     * TODO: the code below is able to retrieve plain text values for AWS
     * creds only. Update the code to support other cred types.
     *
     * @param configurationReader configurator object
     * @return a list of plain text strings to mask
     */
    public static List<String> searchForValuesToMask(ConfigurationReader configurationReader) {
        LOGGER.log(Level.FINE, "Scanning credential store for values to mask");
        List<String> valuesToMask = new ArrayList<String>();
        try {
            Jenkins instance = Jenkins.getInstance();
            if (instance == null) {
                LOGGER.log(Level.FINE, "Jenkins instance is null, cannot retrieve credentials");
                return valuesToMask;
            }

            SystemCredentialsProvider cp =(SystemCredentialsProvider)instance.getExtensionList(
                "com.cloudbees.plugins.credentials.SystemCredentialsProvider").get(0);
            List<Credentials> list = cp.getCredentials();

            for (Credentials cred : list) {
                LOGGER.log(Level.FINE, "Found credential: " + cred.getDescriptor().getId()
                        + " with class: " + cred.getClass().getName());
                boolean maskCredential = configurationReader.isMaskingEnabledForCredential(cred.getDescriptor().getId());
                if (maskCredential != true) {
                    LOGGER.log(Level.FINE, "Credential " + cred.getDescriptor().getId() 
                                            + " skipped as it is not on allow list");
                    continue;
                }

                if (cred instanceof AWSCredentialsImpl) {
                    LOGGER.log(Level.FINE, "Found an AWSCredentialsImpl credential");
                    AWSCredentialsImpl aws = (AWSCredentialsImpl) cred;
                    valuesToMask.add(aws.getSecretKey().getPlainText());
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Exception while retrieving credentials from credential store", e);
        }
        LOGGER.log(Level.INFO,
                "Finished scanning credential store for values to mask, added total of " + valuesToMask.size() + " masks");
        return valuesToMask;
    }
}
