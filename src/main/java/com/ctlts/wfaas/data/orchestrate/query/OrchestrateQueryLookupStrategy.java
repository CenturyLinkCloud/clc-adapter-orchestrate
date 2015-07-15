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
package com.ctlts.wfaas.data.orchestrate.query;

import java.lang.reflect.Method;

import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;

import com.ctlts.wfaas.data.orchestrate.repository.OrchestrateTemplate;

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
        
        QueryMethod queryMethod = new QueryMethod(method, metadata);
        
        if(queryMethod.isPageQuery() || queryMethod.isSliceQuery()) {
            return new OrchestrateRepositoryPagingQuery(method, metadata, orchestrateTemplate);
        }
        
        return new OrchestrateRepositoryQuery(method, metadata, orchestrateTemplate);
        
    }

}
