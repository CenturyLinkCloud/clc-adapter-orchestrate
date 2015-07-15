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
