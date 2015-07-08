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
    private String stringProperty2;
    private TestEntity objectProperty;

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

    public TestEntity getObjectProperty() {
        return objectProperty;
    }

    public void setObjectProperty(TestEntity objectProperty) {
        this.objectProperty = objectProperty;
    }

    public String getStringProperty2() {
        return stringProperty2;
    }

    public void setStringProperty2(String stringProperty2) {
        this.stringProperty2 = stringProperty2;
    }
    
}
