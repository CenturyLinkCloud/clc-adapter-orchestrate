/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import io.orchestrate.client.KvObject;
import io.orchestrate.client.OrchestrateClient;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.util.Assert;

import com.ctlts.wfaas.data.orchestrate.query.Query;
import com.ctlts.wfaas.data.orchestrate.security.EncryptedDeserializer;
import com.ctlts.wfaas.data.orchestrate.security.EncryptedSerializer;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author mramach
 *
 */
@SuppressWarnings("unchecked")
public class OrchestrateTemplate {
    
    private static final int DEFAULT_TIMEOUT = 30;
    
    private String endpoint = "https://api.ctl-uc1-a.orchestrate.io/v0/";
    private String apiKey;
    private int port = 443;
    private boolean useSSL = true;
    private OrchestrateClient client;
    
    @PostConstruct
    public void postConstruct() {
        
        // Initialize the object mapper and register our custom serializer.
        SimpleModule module = new SimpleModule("Encrypted", Version.unknownVersion());
        module.addSerializer(String.class, new EncryptedSerializer());
        module.addDeserializer(String.class, new EncryptedDeserializer());
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        
        client = OrchestrateClient.builder(apiKey)
                .mapper(mapper)
                .host(endpoint)
                .port(port)
                .useSSL(useSSL)
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
        
        return (E)client.kv(collection, id).get(entity.getClass()).get(DEFAULT_TIMEOUT, TimeUnit.SECONDS).getValue();
        
    }

    public <E> List<E> query(String collection, Query query, Class<E> type) {
        return query(collection, query.toString(), type);
    }
    
    public <E> List<E> query(String collection, String query, Class<E> type) {

        Assert.hasLength(collection, "The collection can not be null or an empty String.");
        Assert.notNull(type, "The type can not be null.");

        List<E> results = new LinkedList<E>();

        client.searchCollection(collection).get(type, query).get(DEFAULT_TIMEOUT, TimeUnit.SECONDS).getResults().forEach(sr -> {
            results.add(sr.getKvObject().getValue());
        });

        return results;

    }

    public <E> E findById(String id, Class<E> entityClass, String collection) {
        
        KvObject<E> result = this.client.kv(collection, id).get(entityClass).get(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        
        return result != null ? result.getValue() : null;
        
    }

    public boolean exists(String query, Class<?> entityClass, String collection) {
        return !query(collection, query, entityClass).isEmpty();
    }

    public <T> Iterable<T> findAll(String query, Class<T> entityClass, String collection) {
        return query(collection, query, entityClass);
    }

    public long count(String query, Class<?> entityClass, String collection) {
        return query(collection, query, entityClass).size();
    }

    public void delete(String id, String collection) {
        client.kv(collection, id).delete(Boolean.TRUE).get(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    public void deleteAll(String collection) {
        client.deleteCollection(collection).get(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
