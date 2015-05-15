/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.Id;
import org.springframework.util.Assert;

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

}
