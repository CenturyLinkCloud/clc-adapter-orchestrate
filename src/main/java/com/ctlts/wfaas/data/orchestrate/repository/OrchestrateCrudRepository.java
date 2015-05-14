/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.Assert;

/**
 * @author mramach
 *
 */
public class OrchestrateCrudRepository<T, ID extends Serializable> implements OrchestrateRepository<T, ID> {

    private EntityMetadata entityMetadata;
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
        return null;
    }

    @Override
    public boolean exists(ID id) {
        return false;
    }

    @Override
    public Iterable<T> findAll() {
        return null;
    }

    @Override
    public Iterable<T> findAll(Iterable<ID> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(ID id) {
    }

    @Override
    public void delete(T entity) {
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
    }

    @Override
    public void deleteAll() {
    }

}
