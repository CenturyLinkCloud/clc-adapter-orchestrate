/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

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
