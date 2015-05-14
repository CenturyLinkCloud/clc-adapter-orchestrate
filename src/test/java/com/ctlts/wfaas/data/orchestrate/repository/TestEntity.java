/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.util.UUID;

import org.springframework.data.annotation.Id;

/**
 * @author mramach
 *
 */
@Collection("TestEntity")
public class TestEntity {
    
    @Id
    private String id = UUID.randomUUID().toString();
    private String stringProperty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getStringProperty() {
        return stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

}
