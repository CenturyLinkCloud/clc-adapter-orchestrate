/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.io.Serializable;

import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * @author mramach
 *
 */
public class OrchestrateRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> 
    extends RepositoryFactoryBeanSupport<T, S, ID> {

    private OrchestrateTemplate orchestrateTemplate;
    
    @Override
    protected RepositoryFactorySupport createRepositoryFactory() {
        return new OrchestrateRepositoryFactory(orchestrateTemplate);
    }

    public void setOrchestrateTemplate(OrchestrateTemplate orchestrateTemplate) {
        this.orchestrateTemplate = orchestrateTemplate;
    }

}
