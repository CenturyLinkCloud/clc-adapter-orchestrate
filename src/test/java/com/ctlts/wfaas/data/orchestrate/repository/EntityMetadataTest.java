/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;
import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.PropertyPath;

import com.ctlts.wfaas.data.orchestrate.query.PropertyMetadata;

/**
 * @author mramach
 *
 */
public class EntityMetadataTest {

    @Test
    public void testGetId() {
        
        TestFixtureValid fixture = new TestFixtureValid();
        fixture.setId(UUID.randomUUID().toString());
        
        String id = new EntityMetadata(TestFixtureValid.class).getId(fixture);
        
        assertEquals("Checking that the id value was returned.", fixture.getId(), id);
        
    }
    
    @Test(expected = OrchestrateException.class)
    public void testGetId_NotFound() {
        
        new EntityMetadata(TestFixtureInvalid.class).getId(new TestFixtureInvalid());
        
    }
    
    @Test(expected = OrchestrateException.class)
    public void testGetId_InvalidIdType() {
        
        new EntityMetadata(TestFixtureInvalidIdType.class).getId(new TestFixtureInvalidIdType());
        
    }
    
    @Test
    public void testGetCollection() {
        
        String collection = new EntityMetadata(TestFixtureValid.class).getCollection();
        
        assertEquals("Checking that the id value was returned.",
                TestFixtureValid.class.getSimpleName(), collection);
        
    }
    
    @Test
    public void testGetCollection_WithAnnotation() {
        
        String collection = new EntityMetadata(TestFixtureWithCollectionAnnotation.class).getCollection();
        
        assertEquals("Checking that the id value was returned.", 
                "TestFixture", collection);
        
    }
    
    @Test
    public void testGetPropertyMetadata() throws Exception {
        
        PropertyMetadata metadata = new EntityMetadata(TestFixtureValid.class)
            .getPropertyMetadata(PropertyPath.from("id", TestFixtureValid.class));
        
        assertNotNull("Checking that the property metadata is not null.", metadata);
        
    }
    
    @Test
    public void testGetPropertyMetadata_Nested() throws Exception {
        
        PropertyMetadata metadata = new EntityMetadata(TestFixtureValid.class)
            .getPropertyMetadata(PropertyPath.from("nested.id", TestFixtureNested.class));
        
        assertNotNull("Checking that the property metadata is not null.", metadata);
        assertEquals("Checking that the metadata is for the correct property type.", "id", metadata.getName());
        
    }
    
    public static class TestFixtureValid {
        
        @Id
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
        
    }
    
    public static class TestFixtureInvalid {
    }
    
    public static class TestFixtureInvalidIdType {
        
        @Id
        private Long id;
        
    }
    
    @Collection("TestFixture")
    public static class TestFixtureWithCollectionAnnotation {
    }
    
    public static class TestFixtureNested {
        
        private TestFixtureValid nested;

        public TestFixtureValid getNested() {
            return nested;
        }

        public void setNested(TestFixtureValid nested) {
            this.nested = nested;
        }
        
    }
    
}
