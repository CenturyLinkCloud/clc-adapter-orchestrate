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
package com.ctlts.wfaas.data.orchestrate.repository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.util.Assert;

import com.ctlts.wfaas.data.orchestrate.query.PropertyMetadata;

/**
 * @author mramach
 *
 */
public class EntityMetadata {
    
    private final Class<?> type;

    /**
     * Constructs a new metadata instance for the provided type.
     * 
     * @param type The type this metadata instance will report on.
     */
    public EntityMetadata(Class<?> type) {
        
        Assert.notNull(type, "You must provide a non-null type.");
        
        this.type = type;
        
    }
    
    /**
     * Gets the type.
     * 
     * @return The type thos metadata instance represents.
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Gets the id value from the target.
     * 
     * @param target The target to get the identifier from.
     * @return {@link Serializable} The serializable id value, or 
     * null if the value is not set.
     */
    public String getId(Object target) {
        
        Assert.notNull(target, "The target can not be null.");
        
        Field field = Arrays.stream(type.getDeclaredFields())
            .filter(f -> AnnotationUtils.getAnnotation(f, Id.class) != null)
                .findFirst()
                    .orElseThrow(() -> new OrchestrateException("An @Id has not defined for the class."));
        
        if(!String.class.isAssignableFrom(field.getType())) {
            throw new OrchestrateException("An @Id must be of type String.");
        }
        
        field.setAccessible(true);
        
        try {
            
            return (String) field.get(target);
        
        } catch (Exception e) {
            throw new OrchestrateException("Failed while attempting to access @Id value.", e);
        }
        
    }

    /**
     * Gets the collection the type belongs to. If a Collection annotation is not 
     * present the simple name of the class will be used.
     * 
     * @return String The collection name for the entity.
     */
    public String getCollection() {

        Collection c = AnnotationUtils.findAnnotation(type, Collection.class);
        
        return c != null ? c.value() : type.getSimpleName();
        
    }

    /**
     * Get property metadata for the provided property path.
     * 
     * @param path The path to the property.
     * @return {@link PropertyMetadata} The property metadata instance.
     */
    public PropertyMetadata getPropertyMetadata(PropertyPath path) {
        
        PropertyPath curr = path;
        PropertyMetadata metadata = new PropertyMetadata(path.getOwningType().getType(), path.getSegment());
        
        while(curr.hasNext()) {
            
            curr = path.next();
        
            metadata = new PropertyMetadata(curr.getOwningType().getType(), curr.getSegment());

        }
        
        return metadata;
        
    }

}
