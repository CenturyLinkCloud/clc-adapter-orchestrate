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

import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.ctlts.wfaas.data.orchestrate.repository.OrchestrateTemplate;

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
