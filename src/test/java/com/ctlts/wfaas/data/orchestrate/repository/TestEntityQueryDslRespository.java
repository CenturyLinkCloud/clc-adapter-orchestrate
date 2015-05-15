/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

/**
 * @author mramach
 *
 */
public interface TestEntityQueryDslRespository extends OrchestrateRepository<TestEntity, String> {
    
    public TestEntity findById(String id);

}
