/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

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

    public TestEntity findDistinctByStringProperty(String stringProperty);

    public void findFirst2ByStringProperty(String stringProperty);

    public void countByStringProperty(String stringProperty);

    public void deleteByStringProperty(String stringProperty);

    public Page<TestEntity> findByStringProperty(String stringProperty, Pageable req);

    public Slice<TestEntity> findById(String string, Pageable req);

    public void findByStringPropertyOrderByStringPropertyAsc(String stringProperty);
    
}
