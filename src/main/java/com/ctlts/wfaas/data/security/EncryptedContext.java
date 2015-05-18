/**
 * 
 */
package com.ctlts.wfaas.data.security;

import java.util.Base64;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author mramach
 *
 */
public class EncryptedContext {
    
    private static final ThreadLocal<EncryptedContext> INSTANCE = new InheritableThreadLocal<EncryptedContext>();

    private SecretKey key;
    
    /**
     * Constructs a new encryption context.
     * 
     * @param key The Base 64 encoded key to use in cipher operations.
     */
    public EncryptedContext(byte[] key) {
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
            throw new EncryptedException("Failed while attempting to encrypt value.", e);
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
