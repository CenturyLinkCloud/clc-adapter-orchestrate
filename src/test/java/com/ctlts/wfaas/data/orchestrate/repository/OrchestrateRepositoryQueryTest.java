/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

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
    public void testFindByProperty_FindOne() {
        
        TestEntity t1 = new TestEntity();
        t1.setStringProperty("Hello World!");
        
        repository.save(t1);
        
        TestEntity found = repository.findById(t1.getId());
        
        assertNotNull("Checking that the result is not null.", found);
        
    }
    
    @Test
    public void testFindByProperty_List() {
        
        TestEntity t1 = new TestEntity();
        t1.setStringProperty("testFindByProperty_List 1");
        
        TestEntity t2 = new TestEntity();
        t2.setStringProperty("testFindByProperty_List 2");
        
        repository.save(Arrays.asList(t1, t2));
        
        List<TestEntity> found = repository.findByStringProperty("testFindByProperty_List*");
        
        assertNotNull("Checking that the result is not null.", found);
        
    }
    
    @Test
    public void testFindByNestedProperty() {
        
        TestEntity n1 = new TestEntity();
        n1.setStringProperty("Mark Was Here");
        
        TestEntity t1 = new TestEntity();
        t1.setStringProperty("Hello World!");
        t1.setObjectProperty(n1);
        
        repository.save(t1);
        
        TestEntity found = repository.findByObjectProperty_StringProperty("Mark Was Here");
        
        assertNotNull("Checking that the result is not null.", found);
        
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
