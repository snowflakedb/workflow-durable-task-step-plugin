package io.jenkins.plugins;

import org.junit.*;

import org.junit.Test;

public class ConfigurationReaderTest {

    @Test 
    public void testIsMaskingEnabled() {
        ConfigurationReader configurationReader = new ConfigurationReader();
        Assert.assertTrue(configurationReader.isMaskingEnabled());
    }

    @Test 
    public void testIsMaskingEnabledForCredential() {
        ConfigurationReader configurationReader = new ConfigurationReader();
        Assert.assertTrue(configurationReader.isMaskingEnabledForCredential("test"));
    }
}
