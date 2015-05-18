/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.security;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

/**
 * @author mramach
 *
 */
public class EncryptedSerializer extends JsonSerializer<String> implements ContextualSerializer {

    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) 
            throws IOException, JsonProcessingException {

        jgen.writeString(EncryptedContext.getInstance().encrypt(value));
        
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) 
            throws JsonMappingException {

        if(property.getAnnotation(Encrypted.class) != null) {
            return this;
        }
        
        return new StringSerializer();
        
    }

}
