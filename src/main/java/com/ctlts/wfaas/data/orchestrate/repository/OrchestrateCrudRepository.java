/**
 * 
* Copyright 2015 CenturyLink
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*    http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.Assert;

/**
 * @author mramach
 *
 */
@SuppressWarnings({"unchecked"})
public class OrchestrateCrudRepository<T, ID extends Serializable> implements OrchestrateRepository<T, ID> {

    private EntityMetadata entityMetadata;
    private RepositoryMetadata metadata;
    private OrchestrateTemplate orchestrateTemplate;
    
    public OrchestrateCrudRepository(RepositoryMetadata metadata, OrchestrateTemplate orchestrateTemplate) {
        this.entityMetadata = new EntityMetadata(metadata.getDomainType());
        this.metadata = metadata;
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
        
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::save).collect(Collectors.toList());
        
    }

    @Override
    public T findOne(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        return (T) orchestrateTemplate.findById((String)id, metadata.getDomainType(), entityMetadata.getCollection());
    }

    @Override
    public boolean exists(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        return orchestrateTemplate.exists(getIdQuery((String) id), metadata.getDomainType(), entityMetadata.getCollection());
    }

    @Override
    public Iterable<T> findAll() {
        return (Iterable<T>) orchestrateTemplate.findAll(getQuery(), metadata.getDomainType(), entityMetadata.getCollection());
    }

    @Override
    public Iterable<T> findAll(Iterable<ID> ids) {
        List<String> idStrings = new ArrayList<String>();
        ids.forEach(id -> idStrings.add((String) id));
        return (Iterable<T>) orchestrateTemplate.findAll(getIdsQuery(idStrings), metadata.getDomainType(), entityMetadata.getCollection());
    }

    @Override
    public long count() {
        return orchestrateTemplate.count(getQuery(), metadata.getDomainType(), entityMetadata.getCollection());
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

    private String getIdQuery(String id) {
        return "@path.key:" + id;
    }

    private String getIdsQuery(List<String> ids) {
        return ids.stream().map(id -> String.format("@path.key:\"%s\"", id)).collect(Collectors.joining(" "));
    }

    private String getQuery() {
        return "*";
    }

}
