package com.ctlts.wfaas.adapter.orchestrate;

import io.orchestrate.client.Client;
import io.orchestrate.client.KvObject;
import io.orchestrate.client.OrchestrateClient;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by amoli.ajarekar on 5/11/2015.
 */
@Component
public class OrchestrateAdapter {

    @Value("orchestrate.host")
    private String orchestrateEndpoint;
    @Value("orchestrate.apiKey")
    private String orchestrateApiKey;
    private Client client;

    @PostConstruct
    public void createClient() {
        this.client = OrchestrateClient.builder(orchestrateApiKey)
                .host(orchestrateEndpoint)
                .build();
    }

    @PreDestroy
    public void closeClient() {
        try {
            this.client.close();
        } catch (IOException e) {

        }
    }

    public void save(String collection, String key, Object object) {
        this.client.kv(collection, key).put(object).get();
    }

    public Object findOne(String collection, String key, Class clazz) {
        return ((KvObject)this.client.kv(collection, key).get(clazz).get()).getValue();
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
