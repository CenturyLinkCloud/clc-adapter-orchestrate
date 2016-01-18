/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

/**
 * @author mramach
 *
 */
public class Entity<T> {
    
    private String ref;
    private T value;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
    
    @JsonAnyGetter
    public Map<String,Object> any() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void set(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}
