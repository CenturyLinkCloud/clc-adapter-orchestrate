/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.io.Serializable;

import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.ReflectionEntityInformation;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * @author mramach
 *
 */
@SuppressWarnings("rawtypes")
public class OrchestrateRepositoryFactory extends RepositoryFactorySupport {

    private OrchestrateTemplate orchestrateTemplate;
    
    public OrchestrateRepositoryFactory(OrchestrateTemplate orchestrateTemplate) {
        this.orchestrateTemplate = orchestrateTemplate;
    }

    @Override
    public <T, ID extends Serializable> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        return new ReflectionEntityInformation<T, ID>(domainClass);
    }

    @Override
    protected Object getTargetRepository(RepositoryMetadata metadata) {
        return new OrchestrateCrudRepository(metadata, orchestrateTemplate);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return OrchestrateRepository.class;
    }

}
