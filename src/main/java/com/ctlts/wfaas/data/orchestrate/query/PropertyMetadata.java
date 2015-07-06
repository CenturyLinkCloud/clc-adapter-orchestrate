/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.query;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import org.apache.commons.lang.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;

import com.ctlts.wfaas.data.orchestrate.repository.EntityMetadata;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mramach
 *
 */
public class PropertyMetadata {

    private Field field;
    private PropertyDescriptor descriptor;
    
    public PropertyMetadata(Class<?> type, String name) {
        this.field = FieldUtils.getDeclaredField(type, name, true);
        this.descriptor = BeanUtils.getPropertyDescriptor(type, name);
    }

    public String getName() {
        
        JsonProperty jsonProperty = null;
        
        if(field != null) {
            jsonProperty = field.getAnnotation(JsonProperty.class);
        }
        
        if(jsonProperty == null) {
            jsonProperty = descriptor.getReadMethod().getAnnotation(JsonProperty.class);
        }
        
        if(jsonProperty == null) {
            jsonProperty = descriptor.getWriteMethod().getAnnotation(JsonProperty.class);
        }
        
        return jsonProperty != null ? jsonProperty.value() : descriptor.getName();
        
    }

    public EntityMetadata getFieldMetadata() {
        return new EntityMetadata(descriptor.getPropertyType());
    }

}
