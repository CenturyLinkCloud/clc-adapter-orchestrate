/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ctlts.wfaas.data.orchestrate.query.PropertyMetadata;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mramach
 *
 */
public class PropertyMetadataTest {
   
    @Test
    public void testGetName() {
        
        String name = new PropertyMetadata(TestFixture.class, "value").getName();
        
        assertEquals("Checking that the name is correct.", "value", name);
        
    }
    
    @Test
    public void testGetName_JsonPropertyOverride() {
        
        String name = new PropertyMetadata(TestFixtureFieldOverride.class, "value").getName();
        
        assertEquals("Checking that the name is correct.", "field_override", name);
        
    }
    
    @Test
    public void testGetName_JsonPropertyReadMethodOverride() {
        
        String name = new PropertyMetadata(TestFixtureReadMethodOverride.class, "value").getName();
        
        assertEquals("Checking that the name is correct.", "read_method_override", name);
        
    }
    
    @Test
    public void testGetName_JsonPropertyWriteMethodOverride() {
        
        String name = new PropertyMetadata(TestFixtureWriteMethodOverride.class, "value").getName();
        
        assertEquals("Checking that the name is correct.", "write_method_override", name);
        
    }

    public static class TestFixture {
        
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
        
    }

    public static class TestFixtureFieldOverride {
        
        @JsonProperty("field_override")
        private String value;
    
        public String getValue() {
            return value;
        }
    
        public void setValue(String value) {
            this.value = value;
        }
        
    }
    
    public static class TestFixtureReadMethodOverride {
        
        private String value;
    
        @JsonProperty("read_method_override")
        public String getValue() {
            return value;
        }
    
        public void setValue(String value) {
            this.value = value;
        }
        
    }
    
    public static class TestFixtureWriteMethodOverride {
        
        private String value;
    
        public String getValue() {
            return value;
        }
    
        @JsonProperty("write_method_override")
        public void setValue(String value) {
            this.value = value;
        }
        
    }
    
}
