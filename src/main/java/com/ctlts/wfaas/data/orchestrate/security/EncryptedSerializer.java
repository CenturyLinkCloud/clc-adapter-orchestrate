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
