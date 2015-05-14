/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import io.orchestrate.client.OrchestrateClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author mramach
 *
 */
@SuppressWarnings("unchecked")
public class OrchestrateTemplate {
    
    private String endpoint = "https://api.ctl-uc1-a.orchestrate.io/v0/";
    private String apiKey;
    private OrchestrateClient client;
    
    @PostConstruct
    public void postConstruct() {
        client = OrchestrateClient.builder(apiKey).host(endpoint).build();
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
        
        Assert.hasLength(id, "The id can not be null or an empty String.");
        Assert.notNull(entity, "The entity can not be null.");
        
        client.kv(collection, id).put(entity).get();
        
        return (E)client.kv(collection, id).get(entity.getClass()).get().getValue();
        
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
}
