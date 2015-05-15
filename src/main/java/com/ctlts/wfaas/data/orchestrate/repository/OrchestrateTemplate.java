/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import io.orchestrate.client.OrchestrateClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import io.orchestrate.client.SearchResults;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author mramach
 *
 */
@SuppressWarnings("unchecked")
public class OrchestrateTemplate {
    
    private String endpoint = "https://api.ctl-uc1-a.orchestrate.io/v0/";
    private String apiKey;
    private int port = 443;
    private boolean useSSL = true;
    private OrchestrateClient client;
    
    @PostConstruct
    public void postConstruct() {
        client = OrchestrateClient.builder(apiKey).host(endpoint)
                .port(port).useSSL(useSSL)
                    .build();
    }

    @PreDestroy
    public void preDestroy() throws Exception {
        client.close();
    }

    public <E> E save(String collection, String id, E entity) {
        
        Assert.hasLength(collection, "The collection can not be null or an empty String.");
        Assert.hasLength(id, "The id can not be null or an empty String.");
        Assert.notNull(entity, "The entity can not be null.");

        client.kv(collection, id).put(entity).get();
        
        return (E)client.kv(collection, id).get(entity.getClass()).get(30, TimeUnit.SECONDS).getValue();
        
    }

    public <E> List<E> query(String collection, String query, Class<E> type) {

        Assert.hasLength(collection, "The collection can not be null or an empty String.");
        Assert.notNull(type, "The type can not be null.");

        List<E> results = new LinkedList<E>();

        client.searchCollection(collection).get(type, query).get(30, TimeUnit.SECONDS).getResults().forEach(sr -> {
            results.add(sr.getKvObject().getValue());
        });

        return results;

    }

    public <E> E findById(String id, Class<E> entityClass, String collection) {
        return this.client.kv(collection, id).get(entityClass).get().getValue();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    public boolean exists(String id, Class<?> entityClass, String collection) {
        return this.client.searchCollection(collection)
                .get(entityClass, getIdQuery(id))
                .get().iterator().hasNext();
    }

    public <T> Iterable<T> findAll(Class<T> entityClass, String collection) {
        SearchResults<T> searchResults = this.client.searchCollection(collection)
                .get(entityClass, getQuery())
                .get();

        return StreamSupport.stream(searchResults.spliterator(), false)
                .map(myObjectResult -> myObjectResult.getKvObject().getValue()).collect(Collectors.toList());
    }

    public <T> Iterable<T> findAll(List<String> idStrings, Class<T> entityClass, String collection) {
        SearchResults<T> searchResults = this.client.searchCollection(collection)
                .get(entityClass, getIdsQuery(idStrings))
                .get();

        return StreamSupport.stream(searchResults.spliterator(), false)
                .map(myObjectResult -> myObjectResult.getKvObject().getValue()).collect(Collectors.toList());
    }

    public long count(Class<?> entityClass, String collection) {
        return (this.client.searchCollection(collection)
                .get(entityClass, getQuery())
                .get()).getCount();
    }

    public void delete(String id, String collection) {
        client.kv(collection, id).delete(Boolean.TRUE).get();
    }

    public void deleteAll(String collection) {
        client.deleteCollection(collection);
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    public void setPort(int port) {
        this.port = port;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    private String getIdQuery(String id) {
        return "@path.key:" + id;
    }

    private String getIdsQuery(List<String> ids) {
        return "@path.key:" + ids.stream().collect(Collectors.joining(" "));
    }

    private String getQuery() {
        return "*";
    }

}
