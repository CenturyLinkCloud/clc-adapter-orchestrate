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

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;

/**
 * @author mramach
 *
 */
public class OrchestrateRepositoryConfigurationExtensionSupport extends RepositoryConfigurationExtensionSupport {

    @Override
    public String getRepositoryFactoryClassName() {
        return OrchestrateRepositoryFactoryBean.class.getName();
    }

    @Override
    protected String getModulePrefix() {
        return "orchestrate";
    }

    @Override
    public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {
        builder.addPropertyReference("orchestrateTemplate", config.getAttribute("orchestrateTemplateRef"));
    }

}
