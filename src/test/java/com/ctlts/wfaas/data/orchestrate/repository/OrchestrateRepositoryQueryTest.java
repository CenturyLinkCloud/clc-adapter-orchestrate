/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctlts.wfaas.data.orchestrate.test.OrchestrateMockRule;

/**
 * @author mramach
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class OrchestrateRepositoryQueryTest {

    @Rule
    public OrchestrateMockRule rule = new OrchestrateMockRule();
    
    @Autowired
    private TestEntityQueryDslRespository repository;
    
    @Test
    public void testFindByProperty() {
        
        TestEntity testEntity = new TestEntity();
        testEntity.setStringProperty("Hello World!");
        
        TestEntity result = repository.save(testEntity);
        TestEntity found = repository.findById(result.getId());
        
        assertNotNull("Checking that the result is not null.", found);
        
    }
    
    @Configuration
    @EnableOrchestrateRepositories("com.ctlts.wfaas.data.orchestrate.repository")
    public static class TestConfig {
        
        @Bean
        public OrchestrateTemplate orchestrateTemplate() {
            
            OrchestrateTemplate template = new OrchestrateTemplate();
            template.setEndpoint("http://localhost:8080");
            template.setPort(8080);
            template.setUseSSL(false);
            template.setApiKey("OUR-API-KEY");
            
            return template;
        
        }
        
    }

}
