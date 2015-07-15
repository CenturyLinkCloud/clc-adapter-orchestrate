/**
 * 
* Copyright 2015 CenturyLink
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*    http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
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
