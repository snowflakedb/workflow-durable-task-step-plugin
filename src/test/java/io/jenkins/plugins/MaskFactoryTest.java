package io.jenkins.plugins;

import static org.easymock.EasyMock.*;

import java.io.OutputStream;
import java.util.List;

import org.easymock.*;
import org.junit.*;

import io.jenkins.plugins.ConfigurationReader;
import io.jenkins.plugins.MaskingOutputStream;


public class MaskFactoryTest {
    
    @Test 
    public void testGetMaskedStreamWithMaskingEnabled() {
        ConfigurationReader mockConfigurationReader = EasyMock.createMock(ConfigurationReader.class);

        EasyMock.expect(mockConfigurationReader.isMaskingEnabled()).andReturn(true);        
        EasyMock.expect(mockConfigurationReader.isMaskingEnabledForCredential(anyObject(String.class))).andReturn(false);
        EasyMock.replay(mockConfigurationReader);
                
        OutputStream out = MaskFactory.getMaskedStream(System.out, mockConfigurationReader);                
        Assert.assertTrue(out instanceof MaskingOutputStream);
    }    

    @Test 
    public void testGetMaskedStreamWithMaskingDisabled() {
        ConfigurationReader mockConfigurationReader = EasyMock.createMock(ConfigurationReader.class);

        EasyMock.expect(mockConfigurationReader.isMaskingEnabled()).andReturn(false);
        EasyMock.replay(mockConfigurationReader);

        
        OutputStream out = MaskFactory.getMaskedStream(System.out, mockConfigurationReader);
        Assert.assertFalse(out instanceof MaskingOutputStream);
    }
}
