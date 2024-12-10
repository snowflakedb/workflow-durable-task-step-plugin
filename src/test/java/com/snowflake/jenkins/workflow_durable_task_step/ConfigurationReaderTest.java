package com.snowflake.jenkins.workflow_durable_task_step;

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
