/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
public class OrchestrateCrudRepositoryTest {

    @Autowired
    private TestEntityRespository repository;
    
    @Test
    public void testSave() {
        
        TestEntity testEntity = new TestEntity();
        testEntity.setStringProperty("Hello World!");
        
        TestEntity result = repository.save(testEntity);
        
        assertNotNull("Checking that the result is not null.", result);
        
    }
    
    @Test
    public void testSaveList() {
        
        List<TestEntity> values = Arrays.asList(1, 2).stream()
            .map(v -> {
                
                TestEntity t = new TestEntity();
                t.setStringProperty(String.format("Hello %s time(s)", v));
                
                return t;
                
            }).collect(Collectors.toList());
        
        List<TestEntity> result = (List<TestEntity>) repository.save(values);
        
        assertNotNull("Checking that the result is not null.", result);
        assertEquals("Checking that the list is the expected size.", 2, result.size());
        
    }

    @Test
    public void testFindOne() {

        List<TestEntity> values = Arrays.asList(1, 2).stream()
                .map(v -> {

                    TestEntity t = new TestEntity();
                    t.setStringProperty(String.format("Hello %s time(s)", v));

                    return t;

                }).collect(Collectors.toList());
        repository.save(values);

        TestEntity actual = repository.findOne(values.get(1).getId());

        assertNotNull("Checking that the result is not null.", actual);
        assertEquals("Checking that the list is the expected size.", values.get(1).getId(), actual.getId());
        assertEquals("Checking that the list is the expected size.", values.get(1).getStringProperty(), actual.getStringProperty());

    }
    
    @Configuration
    @EnableOrchestrateRepositories("com.ctlts.wfaas.data.orchestrate.repository")
    public static class TestConfig {
        
        @Bean
        public OrchestrateTemplate orchestrateTemplate() {
            
            OrchestrateTemplate template = new OrchestrateTemplate();
            template.setApiKey("0d1b252d-607c-4fb8-9af5-853b09093679");
            
            return template;
        
        }
        
    }

}
