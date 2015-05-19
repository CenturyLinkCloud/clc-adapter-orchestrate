/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class OrchestrateCrudRepositoryTest {

    @Rule
    public OrchestrateMockRule rule = new OrchestrateMockRule();
    
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

    @Test
    public void testFindAllByIds() {

        List<TestEntity> values = Arrays.asList(1, 2).stream()
                .map(v -> {

                    TestEntity t = new TestEntity();
                    t.setStringProperty(String.format("Hello %s time(s)", v));
                    return t;

                }).collect(Collectors.toList());
        repository.save(values);

        List<TestEntity> actual = (List<TestEntity>) repository.findAll(
                values.stream().map(TestEntity::getId).collect(Collectors.toList()));

        assertNotNull("Checking that the result is not null.", actual);
        assertEquals(2, actual.size());

        Map<String, TestEntity> expected =
                values.stream().collect(toMap(TestEntity::getId,
                        Function.identity()));
        actual.forEach(testEntity -> assertEquals(expected.get(testEntity.getId()).getStringProperty(), testEntity.getStringProperty()));

    }

    @Test
    public void testFindAll() {

        List<TestEntity> values = Arrays.asList(1, 2).stream()
                .map(v -> {

                    TestEntity t = new TestEntity();
                    t.setStringProperty(String.format("Hello %s time(s)", v));
                    return t;

                }).collect(Collectors.toList());
        repository.save(values);

        List<TestEntity> actual = (List<TestEntity>) repository.findAll();

        assertNotNull("Checking that the result is not null.", actual);
        assertEquals(2, actual.size());

        Map<String, TestEntity> expected =
                values.stream().collect(toMap(TestEntity::getId,
                        Function.identity()));
        actual.forEach(testEntity -> assertEquals(expected.get(testEntity.getId()).getStringProperty(), testEntity.getStringProperty()));

    }

    @Test
    public void testExists() {

        List<TestEntity> values = Arrays.asList(1, 2).stream()
                .map(v -> {

                    TestEntity t = new TestEntity();
                    t.setStringProperty(String.format("Hello %s time(s)", v));
                    return t;

                }).collect(Collectors.toList());
        repository.save(values);

        assertTrue(repository.exists(values.get(1).getId()));
        assertFalse(repository.exists("3"));

    }

    @Test
    public void testCount() {

        List<TestEntity> values = Arrays.asList(1, 2).stream()
                .map(v -> {

                    TestEntity t = new TestEntity();
                    t.setStringProperty(String.format("Hello %s time(s)", v));
                    return t;

                }).collect(Collectors.toList());
        repository.save(values);

        assertThat(repository.count(), equalTo(2L));
    }

    @Test
    public void testDeleteById() {

        List<TestEntity> values = Arrays.asList(1, 2).stream()
                .map(v -> {

                    TestEntity t = new TestEntity();
                    t.setStringProperty(String.format("Hello %s time(s)", v));
                    return t;

                }).collect(Collectors.toList());
        
        repository.save(values);
        repository.delete(values.get(0).getId());
        
        List<TestEntity> actual = (List<TestEntity>) repository.findAll(
                values.stream().map(v -> v.getId()).collect(Collectors.toList()));

        assertNotNull("Checking that the result is not null.", actual);
        assertEquals(1, actual.size());
        
    }

    @Test
    public void testDeleteEntity() {

        TestEntity t = new TestEntity();
        t.setStringProperty(String.format("Hello World!"));
        
        repository.save(t);
        repository.delete(Arrays.asList(t));
        
        TestEntity actual = repository.findById(t.getId());

        assertNull("Checking that the result is null.", actual);

    }

    @Test
    public void testDeleteEntities() {

        TestEntity t1 = new TestEntity();
        t1.setStringProperty(String.format("1"));
        
        TestEntity t2 = new TestEntity();
        t2.setStringProperty(String.format("1"));
        
        repository.save(Arrays.asList(t1, t2));
        repository.delete(Arrays.asList(t1, t2));
        
        assertNull("Checking that the result is null.", repository.findById(t1.getId()));
        assertNull("Checking that the result is null.", repository.findById(t2.getId()));

    }

    @Test
    public void testDeleteAllEntities() {

        TestEntity t1 = new TestEntity();
        t1.setStringProperty(String.format("1"));
        
        TestEntity t2 = new TestEntity();
        t2.setStringProperty(String.format("1"));
        
        repository.save(Arrays.asList(t1, t2));
        repository.deleteAll();
        
        assertNull("Checking that the result is null.", repository.findById(t1.getId()));
        assertNull("Checking that the result is null.", repository.findById(t2.getId()));
        
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
