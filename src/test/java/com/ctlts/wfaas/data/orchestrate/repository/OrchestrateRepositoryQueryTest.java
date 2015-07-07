/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctlts.wfaas.data.orchestrate.config.EnableOrchestrateRepositories;
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
    
    /**
     * Because of an issue with wiremock, specifically related to PUT operations
     * following GET operations, I'm adding this before block. If the save op fails,
     * it should only fail the first time.
     */
    @Before
    public void before() {
        
        TestEntity e = new TestEntity();
        
        try {
            
            repository.save(e);
            
        } catch (Exception e1) {}
        
        try {
            
            repository.delete(e);
            
        } catch (Exception e1) {}
        
    }
    
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
    
    @Test
    public void testFindBy_WithAnd() {
        
        TestEntity n1 = new TestEntity();
        n1.setStringProperty("Mark Was Here");
        
        TestEntity t1 = new TestEntity();
        t1.setStringProperty("Hello World!");
        t1.setObjectProperty(n1);
        
        repository.save(t1);
        
        TestEntity found = repository.findByStringPropertyAndObjectProperty_StringProperty(
                "Hello World!", "Mark Was Here");
        
        assertNotNull("Checking that the result is not null.", found);
        
    }
    
    @Test
    public void testFindBy_WithOr() {
        
        TestEntity n1 = new TestEntity();
        n1.setStringProperty("Mark Was Here");
        
        TestEntity t1 = new TestEntity();
        t1.setStringProperty("Hello World!");
        t1.setObjectProperty(n1);
        
        repository.save(t1);
        
        TestEntity found = repository.findByStringPropertyOrObjectProperty_StringProperty(
                "Hello World!", "Mark Was Not Here");
        
        assertNotNull("Checking that the result is not null.", found);
        
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFindDistinctBy() {
        repository.findDistinctByStringProperty("Distinct is not supported.");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFindLimitingBy() {
        repository.findFirst2ByStringProperty("Limiting is not supported.");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCountBy() {
        repository.countByStringProperty("Count is not supported.");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteBy() {
        repository.deleteByStringProperty("Delete/Remove is not supported.");
    }
    
    @Test
    public void findBy_WithPaging() {
        
        TestEntity n1 = new TestEntity();
        n1.setStringProperty("similar");
        
        TestEntity n2 = new TestEntity();
        n2.setStringProperty("similar");
        
        repository.save(Arrays.asList(n1, n2));
        
        Page<TestEntity> page = repository.findByStringProperty("similar", new PageRequest(0, 1));
        
        assertNotNull("Checking that the page result is not null.", page);
        assertEquals("Checking that the page is the correct size.", 1, page.getSize());
        assertEquals("Checking that the total size is correct.", 2, page.getTotalElements());
        assertTrue("Checking that the next page is available.", page.hasNext());
        assertFalse("Checking that the previous page is not available.", page.hasPrevious());
        
        Page<TestEntity> page2 = repository.findByStringProperty("similar", page.nextPageable());
        
        assertNotNull("Checking that the page result is not null.", page2);
        assertEquals("Checking that the page is the correct size.", 1, page2.getSize());
        assertEquals("Checking that the total size is correct.", 2, page2.getTotalElements());
        assertFalse("Checking that the next page is not available.", page2.hasNext());
        assertTrue("Checking that the previous page is available.", page2.hasPrevious());
        
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void findBy_WithSlice() {
        repository.findById("Slice is not supported.", new PageRequest(1, 2));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testFindBy_WithOrder() {
        repository.findByStringPropertyOrderByStringPropertyAsc("Order By is not supported.");
    }
    
    @Configuration
    @EnableOrchestrateRepositories("com.ctlts.wfaas.data.orchestrate.repository")
    public static class TestConfig {
        
        @Bean
        public OrchestrateTemplate orchestrateTemplate() {
            
            OrchestrateTemplate template = new OrchestrateTemplate();
            template.setEndpoint("http://localhost:5124/v0");
            template.setPort(5124);
            template.setUseSSL(false);
            template.setApiKey("OUR-API-KEY");
            
            return template;
        
        }
        
    }

}
