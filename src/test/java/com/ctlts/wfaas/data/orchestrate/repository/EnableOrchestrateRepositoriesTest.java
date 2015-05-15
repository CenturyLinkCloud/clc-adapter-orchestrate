/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author mramach
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class EnableOrchestrateRepositoriesTest {

    @Autowired
    private TestEntityRespository repository;
    
    @Test
    public void testRepositoryConfigured() {
        assertNotNull("Checking that the repository was created and injected.", repository);
    }
    
    @Configuration
    @EnableOrchestrateRepositories("com.ctlts.wfaas.data.orchestrate.repository")
    public static class TestConfig {
        
        @Bean
        public OrchestrateTemplate orchestrateTemplate() {
            
            OrchestrateTemplate template = new OrchestrateTemplate();
            template.setEndpoint("http://localhost:5124");
            template.setPort(5124);
            template.setUseSSL(false);
            template.setApiKey("OUR-API-KEY");
            
            return template;
            
        }
        
    }
    
}
