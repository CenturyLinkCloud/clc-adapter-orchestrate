/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.Assert;

/**
 * @author mramach
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrchestrateCrudRepository<T, ID extends Serializable> implements OrchestrateRepository<T, ID> {

    private EntityMetadata<T, ID> entityMetadata;
    private OrchestrateTemplate orchestrateTemplate;
    
    public OrchestrateCrudRepository(RepositoryMetadata metadata, OrchestrateTemplate orchestrateTemplate) {
        this.entityMetadata = new EntityMetadata(metadata.getDomainType());
        this.orchestrateTemplate = orchestrateTemplate;
    }

    @Override
    public <S extends T> S save(S entity) {
        
        String collection = entityMetadata.getCollection();
        String id = (String)entityMetadata.getId(entity);
        
        return orchestrateTemplate.save(collection, id, entity);
        
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        
        Assert.notNull(entities, "The entities collection can not be null.");
        List<S> results = new LinkedList<S>();

        entities.iterator().forEachRemaining(e -> {
            results.add(save(e));
        });
        
        return results;
        
    }

    @Override
    public T findOne(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        return (T) orchestrateTemplate.findById((String)id, entityMetadata.getType(), entityMetadata.getCollection());
    }

    @Override
    public boolean exists(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        return orchestrateTemplate.exists((String)id, entityMetadata.getType(), entityMetadata.getCollection());
    }

    @Override
    public Iterable<T> findAll() {
        return orchestrateTemplate.findAll(entityMetadata.getType(), entityMetadata.getCollection());
    }

    @Override
    public Iterable<T> findAll(Iterable<ID> ids) {
        List<String> idStrings = new ArrayList<String>();
        ids.forEach(id -> idStrings.add((String) id));
        return orchestrateTemplate.findAll(idStrings, entityMetadata.getType(), entityMetadata.getCollection());
    }

    @Override
    public long count() {
        return orchestrateTemplate.count(entityMetadata.getType(), entityMetadata.getCollection());
    }

    @Override
    public void delete(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        orchestrateTemplate.delete((String) id, entityMetadata.getCollection());
    }

    @Override
    public void delete(T entity) {
        Assert.notNull(entity, "The given entity must not be null!");
        orchestrateTemplate.delete(entityMetadata.getId(entity), entityMetadata.getCollection());
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        for (T entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        orchestrateTemplate.deleteAll(entityMetadata.getCollection());
    }

}
