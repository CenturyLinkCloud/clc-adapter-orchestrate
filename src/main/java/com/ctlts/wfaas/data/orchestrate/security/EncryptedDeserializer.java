/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.security;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

/**
 * @author mramach
 *
 */
public class EncryptedDeserializer extends JsonDeserializer<String> implements ContextualDeserializer {

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) 
            throws IOException, JsonProcessingException {

        return EncryptedContext.getInstance().decrypt(jp.readValueAs(String.class));
        
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) 
            throws JsonMappingException {

        if(property != null && property.getAnnotation(Encrypted.class) != null) {
            return this;
        }
        
        return new StringDeserializer();
        
    }

}
