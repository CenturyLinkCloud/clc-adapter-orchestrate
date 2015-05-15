/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.config;

import java.io.Serializable;

import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.ReflectionEntityInformation;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;

import com.ctlts.wfaas.data.orchestrate.query.OrchestrateQueryLookupStrategy;
import com.ctlts.wfaas.data.orchestrate.repository.OrchestrateCrudRepository;
import com.ctlts.wfaas.data.orchestrate.repository.OrchestrateRepository;
import com.ctlts.wfaas.data.orchestrate.repository.OrchestrateTemplate;

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
    
    @Override
    protected QueryLookupStrategy getQueryLookupStrategy(Key key,
            EvaluationContextProvider evaluationContextProvider) {

        return new OrchestrateQueryLookupStrategy(orchestrateTemplate);
        
    }

//    private static boolean isQueryDslRepository(Class<?> repositoryInterface) {
//        return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
//    }

}
