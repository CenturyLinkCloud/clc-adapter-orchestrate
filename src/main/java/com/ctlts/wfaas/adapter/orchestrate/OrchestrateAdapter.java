package com.ctlts.wfaas.adapter.orchestrate;

import io.orchestrate.client.Client;
import io.orchestrate.client.KvObject;
import io.orchestrate.client.OrchestrateClient;
import io.orchestrate.client.dao.GenericAsyncDao;

/**
 * Created by amoli.ajarekar on 5/11/2015.
 */
public class OrchestrateAdapter {

    private String orchestrateEndpoint = "https://api.ctl-uc1-a.orchestrate.io/v0/";
    private String orchestrateApiKey = "dc8f355e-75c1-44aa-a2a1-f8e64a26254c";
    private Client client;

    public OrchestrateAdapter() {
        this.client = OrchestrateClient.builder(orchestrateApiKey)
                .host(orchestrateEndpoint)
                .build();
    }

    public OrchestrateAdapter(Client client) {
        this.client = client;
    }

    public void save(String collection, String key, Object object) {
        this.client.kv(collection, key).put(object).get();
    }

    public Object findOne(String collection, String key, Class clazz) {
        return ((KvObject)this.client.kv(collection, key).get(clazz).get()).getValue();
    }




}
