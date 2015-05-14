/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.lang.annotation.Annotation;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/**
 * @author mramach
 *
 */
public class OrchestrateRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableOrchestrateRepositories.class;
    }

    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new OrchestrateRepositoryConfigurationExtensionSupport();
    }

}
