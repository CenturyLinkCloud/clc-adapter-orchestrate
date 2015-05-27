/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.security;

import java.util.Base64;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author mramach
 *
 */
public class EncryptedContext {
    
    private static final ThreadLocal<EncryptedContext> INSTANCE = new InheritableThreadLocal<EncryptedContext>();

    private byte[] encodedKey;
    private SecretKey key;
    
    /**
     * Constructs a new encryption context.
     * 
     * @param key The Base 64 encoded key to use in cipher operations.
     */
    public EncryptedContext(byte[] key) {
        this.encodedKey = key;
        this.key = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
    }

    /**
     * Encrypt the provided value.
     * 
     * @param value The value to encrypt.
     * @return The Base64 encoded encrypted value.
     */
    public String encrypt(String value) {
        
        try {
            
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, key);
            
            return Base64.getEncoder().encodeToString(c.doFinal(value.getBytes()));
            
        } catch (Exception e) {
            throw new EncryptedException("Failed while attempting to encrypt value.", e);
        }
        
    }
    
    /**
     * Decrypt the provided value.
     * 
     * @param value The Base64 encoded value to decrypt.
     * @return The decrypted value.
     */
    public String decrypt(String value) {
        
        try {
            
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, key);
            
            return new String(c.doFinal(Base64.getDecoder().decode(value)));
            
        } catch (Exception e) {
            throw new EncryptedException("Failed while attempting to decrypt value.", e);
        }
        
    }
    
    /**
     * @return the Base64 encoded key value.
     */
    public byte[] getEncodedKey() {
        return encodedKey;
    }

    /**
     * Creates a new encryption context, initializing a new key for cipher operations.
     * 
     * @return {@link EncryptedContext} A new instance.
     */
    public static EncryptedContext create() {
        
        try {
            
            // Initialize a secret key for the instance.
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            
            SecretKey key = keyGen.generateKey();
            
            byte[] encodedKey = Base64.getEncoder().encode(key.getEncoded());
            
            return new EncryptedContext(encodedKey);
            
        } catch (Exception e) {
            throw new EncryptedException("Failed while attempting to generate a ne encryption key.", e);
        }
        
    }
    
    /**
     * Provides access to the current encrypted context instance.
     * 
     * @return The current encrypted context instance.
     */
    public static EncryptedContext getInstance() {
        return Optional.ofNullable(INSTANCE.get()).orElseThrow(() -> new EncryptedException(
                "No encryption context has been defined for this operation."));
    }

    /**
     * Sets the current encrypted context instance.
     * 
     * @param value The new encrypted context.
     */
    public static void setInstance(EncryptedContext value) {
        INSTANCE.set(value);
    }

}
