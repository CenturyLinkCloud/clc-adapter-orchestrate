/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import io.orchestrate.client.OrchestrateClient;

import java.net.URI;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    private ObjectMapper mapper;
    
    @PostConstruct
    public void postConstruct() {
        
        // Initialize the object mapper and register our custom serializer.
        SimpleModule module = new SimpleModule("Encrypted", Version.unknownVersion());
        module.addSerializer(String.class, new EncryptedSerializer());
        module.addDeserializer(String.class, new EncryptedDeserializer());
        
        mapper = new ObjectMapper();
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

    @SuppressWarnings("rawtypes")
    public <E> E save(String collection, String id, E entity) {
        
        Assert.hasLength(collection, "The collection can not be null or an empty String.");
        Assert.hasLength(id, "The id can not be null or an empty String.");
        Assert.notNull(entity, "The entity can not be null.");

        URI uri = UriComponentsBuilder.fromHttpUrl(endpoint)
                .path("/" + collection + "/" + id)
                        .build()
                            .toUri();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(apiKey.getBytes()));
        headers.add("If-None-Match", "*");
        headers.add("Content-Type", "application/json");
        
        try {
            
            new RestTemplate().exchange(uri, HttpMethod.PUT, 
                    new HttpEntity(mapper.writeValueAsString(entity), headers), Map.class);
            
            return (E) findById(id, entity.getClass(), collection);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
        
    }

    public <E> List<E> query(String collection, Query query, Class<E> type) {
        return query(collection, query.toString(), type);
    }
    
    @SuppressWarnings("rawtypes")
    public <E> List<E> query(String collection, String query, Class<E> type) {

        Assert.hasLength(collection, "The collection can not be null or an empty String.");
        Assert.notNull(type, "The type can not be null.");

        List<E> results = new LinkedList<E>();

        URI uri = UriComponentsBuilder.fromHttpUrl(endpoint)
            .path("/" + collection)
                .queryParam("query", query)
                    .build()
                        .toUri();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(apiKey.getBytes()));
        
        HttpEntity<?> entity = new HttpEntity(headers);
        
        ResponseEntity<Map> res = new RestTemplate().exchange(uri, HttpMethod.GET, entity, Map.class);
        
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) res.getBody().get("results");
        
        resultList.forEach(i -> {
                
            try {
                
                results.add(mapper.readValue(new ObjectMapper().writeValueAsString(i.get("value")), type));
                    
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
                
        });
        
        return results;

    }

    @SuppressWarnings("rawtypes")
    public <E> E findById(String id, Class<E> entityClass, String collection) {
        
        URI uri = UriComponentsBuilder.fromHttpUrl(endpoint)
                .path("/" + collection + "/" + id)
                        .build()
                            .toUri();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(apiKey.getBytes()));
        
        ResponseEntity<Map> res = new RestTemplate().exchange(uri, HttpMethod.GET, new HttpEntity(headers), Map.class);
        
        try {
            
            return mapper.readValue(new ObjectMapper().writeValueAsString(res.getBody()), entityClass);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
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
