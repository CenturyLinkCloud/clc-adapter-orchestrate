/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.security;

import static org.junit.Assert.*;

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

}
