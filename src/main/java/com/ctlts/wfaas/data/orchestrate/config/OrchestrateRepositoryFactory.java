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

}
