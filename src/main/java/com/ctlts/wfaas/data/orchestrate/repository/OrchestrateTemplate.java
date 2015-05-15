/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import io.orchestrate.client.OrchestrateClient;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.util.Assert;

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
    public void preDestroy() {
        try {
            client.close();
        } catch (IOException e) {
            //do nothing
        }
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
        Assert.hasLength(id, "The id can not be null or an empty String.");
        return this.client.kv(collection, id).get(entityClass).get().getValue();
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public <ID extends Serializable> void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

}
