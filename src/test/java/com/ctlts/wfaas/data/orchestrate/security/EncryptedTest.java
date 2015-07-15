/**
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
 * 
 */
package com.ctlts.wfaas.data.orchestrate.security;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author mramach
 *
 */
public class EncryptedTest {
    
    private ObjectMapper mapper;
    
    @Before
    public void before() throws Exception {

        // Initialize the object mapper and register our custom serializer.
        SimpleModule module = new SimpleModule("Encrypted", Version.unknownVersion());
        module.addSerializer(String.class, new EncryptedSerializer());
        module.addDeserializer(String.class, new EncryptedDeserializer());
        
        mapper = new ObjectMapper();
        mapper.registerModule(module);
        
        EncryptedContext.setInstance(EncryptedContext.create());
        
    }
    
    @Test
    public void testEncryptedSerialize_Field() throws Exception {
        
        TestFixture tf = new TestFixture();
        tf.plainText = "Hello";
        tf.encryptedText = "World!";
        
        String json = mapper.writeValueAsString(tf);
        
        assertTrue("Checking that the json string does contain the "
                + "pre-encrypted text value.", json.contains("Hello"));
        
        assertFalse("Checking that the json string does not contain the "
                + "pre-encrypted text value.", json.contains("World!"));
        
    }
    
    @Test
    public void testEncryptedSerialize_Method() throws Exception {
        
        TestFixture2 tf = new TestFixture2();
        tf.setPlainText("Hello");
        tf.setEncryptedText("World!");
        
        String json = mapper.writeValueAsString(tf);
        
        assertTrue("Checking that the json string does contain the "
                + "pre-encrypted text value.", json.contains("Hello"));
        
        assertFalse("Checking that the json string does not contain the "
                + "pre-encrypted text value.", json.contains("World!"));
        
    }
    
    @Test
    public void testEncryptedDeserialize_Field() throws Exception {
        
        TestFixture tf = new TestFixture();
        tf.plainText = "Hello";
        tf.encryptedText = "World!";
        
        String json = mapper.writeValueAsString(tf);
        
        TestFixture deserialized = mapper.readValue(json.getBytes(), TestFixture.class);
        
        assertEquals("Checking that the json was deserialized properly.", "Hello", deserialized.plainText);
        assertEquals("Checking that the json was deserialized properly.", "World!", deserialized.encryptedText);
        
    }
    
    @Test
    public void testEncryptedDeserialize_Method() throws Exception {
        
        TestFixture2 tf = new TestFixture2();
        tf.setPlainText("Hello");
        tf.setEncryptedText("World!");
        
        String json = mapper.writeValueAsString(tf);
        
        TestFixture deserialized = mapper.readValue(json.getBytes(), TestFixture.class);
        
        assertEquals("Checking that the json was deserialized properly.", "Hello", deserialized.plainText);
        assertEquals("Checking that the json was deserialized properly.", "World!", deserialized.encryptedText);
        
    }
    
    @Test(expected = JsonMappingException.class)
    public void testEncryptedContextNotSet() throws Exception {
        
        EncryptedContext.setInstance(null);
        
        TestFixture tf = new TestFixture();
        tf.plainText = "Hello";
        tf.encryptedText = "World!";
        
        mapper.writeValueAsString(tf);
        
    }
    
    @Test
    public void testEncryptedSerialize_Map() throws Exception {
        
        TestFixtureMap tf = new TestFixtureMap();
        tf.getMap().put("hello", "world");
        
        String json = mapper.writeValueAsString(tf);
        
        assertTrue("Checking that the json string does contain the "
                + "pre-encrypted text value.", json.contains("hello"));
        
        assertFalse("Checking that the json string does not contain the "
                + "pre-encrypted text value.", json.contains("world"));
        
    }
    
    @Test
    public void testEncryptedDeserialize_Map() throws Exception {
        
        TestFixtureMap tf = new TestFixtureMap();
        tf.getMap().put("hello", "world");
        
        String json = mapper.writeValueAsString(tf);
        
        TestFixtureMap deserialized = mapper.readValue(json.getBytes(), TestFixtureMap.class);
        
        assertEquals("Checking that the json was deserialized properly.", "world", deserialized.getMap().get("hello"));
        
    }
    
    public static class TestFixture {
        
        public String plainText;
        @Encrypted
        public String encryptedText;
        
    }
    
    public static class TestFixture2 {
        
        private String plainText;
        private String encryptedText;
        
        public String getPlainText() {
            return plainText;
        }
        
        public void setPlainText(String plainText) {
            this.plainText = plainText;
        }

        public String getEncryptedText() {
            return encryptedText;
        }

        @Encrypted
        public void setEncryptedText(String encryptedText) {
            this.encryptedText = encryptedText;
        }
        
    }
    
    
    public static class TestFixtureMap {
        
        @Encrypted
        public Map<String, Object> map = new LinkedHashMap<String, Object>();

        public Map<String, Object> getMap() {
            return map;
        }

        public void setMap(Map<String, Object> map) {
            this.map = map;
        }
        
    }

}
