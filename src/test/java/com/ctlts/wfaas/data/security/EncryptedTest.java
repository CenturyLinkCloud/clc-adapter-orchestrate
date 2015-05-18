/**
 * 
 */
package com.ctlts.wfaas.data.security;

import static org.junit.Assert.*;

import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author mramach
 *
 */
public class EncryptedTest {
    
    @Before
    public void before() throws Exception {
        
        // Initialize a secret key for the test.
        
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        
        SecretKey key = keyGen.generateKey();
        
        byte[] encodedKey = Base64.getEncoder().encode(key.getEncoded());
        
        EncryptedContext.setInstance(new EncryptedContext(encodedKey));
        
    }
    
    @Test
    public void testEncyptedSerialize() throws Exception {
        
        TestFixture tf = new TestFixture();
        tf.plainText = "Hello";
        tf.encryptedText = "World!";
        
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(tf);
        
        assertTrue("Checking that the json string does contain the "
                + "pre-encrypted text value.", json.contains("Hello"));
        
        assertFalse("Checking that the json string does not contain the "
                + "pre-encrypted text value.", json.contains("World!"));
        
    }
    
    @Test
    public void testEncyptedDeserialize() throws Exception {
        
        TestFixture tf = new TestFixture();
        tf.plainText = "Hello";
        tf.encryptedText = "World!";
        
        ObjectMapper mapper = new ObjectMapper();
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
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValueAsString(tf);
        
    }
    
    public static class TestFixture {
        
        public String plainText;
        @JsonSerialize(using = EncryptedSerializer.class)
        @JsonDeserialize(using = EncryptedDeserializer.class)
        public String encryptedText;
        
    }

}
