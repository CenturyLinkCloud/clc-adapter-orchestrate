/**
 * 
 */
package com.ctlts.wfaas.data.security;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author mramach
 *
 */
public class EncryptedDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) 
            throws IOException, JsonProcessingException {

        return EncryptedContext.getInstance().decrypt(jp.readValueAs(String.class));
        
    }

}
