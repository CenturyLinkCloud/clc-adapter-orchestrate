/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.lang.reflect.Method;

import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;

/**
 * @author mramach
 *
 */
public class OrchestrateQueryLookupStrategy implements QueryLookupStrategy {

    private OrchestrateTemplate orchestrateTemplate;
    
    public OrchestrateQueryLookupStrategy(OrchestrateTemplate orchestrateTemplate) {
        this.orchestrateTemplate = orchestrateTemplate;
    }

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, NamedQueries namedQueries) {
        return new OrchestrateRepositoryQuery(method, metadata, orchestrateTemplate);
    }

}
