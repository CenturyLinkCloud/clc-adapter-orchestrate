/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.util.List;

/**
 * @author mramach
 *
 */
public interface TestEntityQueryDslRespository extends OrchestrateRepository<TestEntity, String> {
    
    public TestEntity findById(String id);

    public List<TestEntity> findByStringProperty(String stringProperty);

    public TestEntity findByObjectProperty_StringProperty(String stringProperty);

    public TestEntity findByStringPropertyAndObjectProperty_StringProperty(
            String stringProperty, String nestedStringProeprty);
    
    public TestEntity findByStringPropertyOrObjectProperty_StringProperty(
            String stringProperty, String nestedStringProeprty);

}
