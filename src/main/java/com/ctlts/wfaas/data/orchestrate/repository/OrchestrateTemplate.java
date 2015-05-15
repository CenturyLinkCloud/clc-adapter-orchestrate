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
    private int port = 80;
    private boolean useSSL = true;
    private OrchestrateClient client;
    
    @PostConstruct
    public void postConstruct() {
        client = OrchestrateClient.builder(apiKey).host(endpoint).port(port).useSSL(useSSL).build();
    }

    @PreDestroy
    public void preDestroy() {
        try {
            client.close();
        } catch (IOException e) {
            //do nothing
        }
    }

    public <E> E save(String collection, String id, E entity) {
        client.kv(collection, id).put(entity).get();
        return (E)client.kv(collection, id).get(entity.getClass()).get().getValue();
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

    public boolean exists(String id, String collection) {
        return this.client.searchCollection(collection)
                .get(String.class, getIdQuery(id))
                .get().iterator().hasNext();
    }

    public <T> Iterable<T> findAll(Class<T> entityClass, String collection) {
        SearchResults<T> searchResults = this.client.searchCollection(collection)
                .get(entityClass, getQuery())
                .get();

        return StreamSupport.stream(searchResults.spliterator(), false)
                .map(myObjectResult -> myObjectResult.getKvObject().getValue()).collect(Collectors.toList());
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public <ID extends Serializable> void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    private String getIdQuery(String id) {
        return "@path.key:" + id;
    }

    private String getQuery() {
        return "*";
    }

}
