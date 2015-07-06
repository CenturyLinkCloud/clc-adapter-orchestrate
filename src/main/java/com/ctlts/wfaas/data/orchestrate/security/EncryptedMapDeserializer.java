/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * @author mramach
 *
 */
public class EncryptedMapDeserializer extends MapDeserializer {

    private static final long serialVersionUID = 7758704915770859891L;

    public EncryptedMapDeserializer(JavaType mapType,
            ValueInstantiator valueInstantiator, KeyDeserializer keyDeser,
            JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser) {
        super(mapType, valueInstantiator, keyDeser, valueDeser, valueTypeDeser);
    }

    public EncryptedMapDeserializer(MapDeserializer src,
            KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser,
            TypeDeserializer valueTypeDeser, HashSet<String> ignorable) {
        super(src, keyDeser, valueDeser, valueTypeDeser, ignorable);
    }

    public EncryptedMapDeserializer(MapDeserializer src) {
        super(src);
    }

    @Override
    public Map<Object, Object> deserialize(JsonParser jp, DeserializationContext ctxt) 
            throws IOException, JsonProcessingException {

        return super.deserialize(jp, ctxt);
        
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) 
            throws JsonMappingException {

        return super.createContextual(ctxt, property);
        
    }

}
