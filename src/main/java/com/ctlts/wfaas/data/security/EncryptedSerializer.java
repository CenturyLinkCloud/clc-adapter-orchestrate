/**
 * 
 */
package com.ctlts.wfaas.data.security;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author mramach
 *
 */
public class EncryptedSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) 
            throws IOException, JsonProcessingException {

        jgen.writeString(EncryptedContext.getInstance().encrypt(value));
        
    }

}
