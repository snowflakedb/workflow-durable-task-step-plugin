package io.jenkins.plugins;

import java.io.OutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jenkins.model.Jenkins;

import com.cloudbees.plugins.credentials.domains.*;
import com.cloudbees.plugins.credentials.CredentialsStore;
import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import com.cloudbees.jenkins.plugins.awscredentials.AWSCredentialsImpl;

public class MaskFactory {

    private static final Logger LOGGER = Logger.getLogger(MaskFactory.class.getName());    

    public static OutputStream getMaskedStream(OutputStream delegate) {
        ConfigurationReader configurationReader = new ConfigurationReader();
        return getMaskedStream(delegate, configurationReader);
    }

    public static OutputStream getMaskedStream(OutputStream delegate, ConfigurationReader configurationReader) {        
        boolean enableCredentialsMasking = configurationReader.isMaskingEnabled();        
        LOGGER.log(Level.INFO, "Creating MaskingOutputStream " + enableCredentialsMasking);            
        if (enableCredentialsMasking == true) {            
            LOGGER.log(Level.INFO, "Creating MaskingOutputStream");            
            List<String> valuesToMask = searchForValuesToMask(configurationReader);                        
            return new MaskingOutputStream(delegate, valuesToMask);
        }
        else {
            LOGGER.log(Level.INFO, "No masking applied");
            return delegate;
        }
    }

    public static List<String> searchForValuesToMask(ConfigurationReader configurationReader) {
        LOGGER.log(Level.INFO, "Scanning credential store for values to mask");
        List<String> valuesToMask = new ArrayList<String>();
        try {
            Iterable<CredentialsStore> stores = CredentialsProvider.lookupStores(Jenkins.getInstance());
            for (CredentialsStore cs : stores) {
                List<Credentials> list = cs.getCredentials(Domain.global());
                for (Credentials cred : list) {                                
                    boolean maskCredential = configurationReader.isMaskingEnabledForCredential(cred.getDescriptor().getId());
                    if (!maskCredential) {
                        continue;
                    }

                    if (cred instanceof UsernamePasswordCredentialsImpl) {
                        LOGGER.log(Level.FINE, "Found a UsernamePasswordCredentialsImpl credential");
                        UsernamePasswordCredentialsImpl upc = (UsernamePasswordCredentialsImpl) cred;                    
                        valuesToMask.add(upc.getUsername());
                        valuesToMask.add(upc.getPassword().getPlainText());
                    }

                    if (cred instanceof AWSCredentialsImpl) {
                        LOGGER.log(Level.FINE, "Found an AWSCredentialsImpl credential");
                        AWSCredentialsImpl aws = (AWSCredentialsImpl) cred;
                        valuesToMask.add(aws.getSecretKey().getPlainText());
                    }
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
