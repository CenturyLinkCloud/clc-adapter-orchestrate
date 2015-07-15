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
