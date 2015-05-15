package com.ctlts.wfaas.adapter.orchestrate;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import io.orchestrate.client.Client;
import io.orchestrate.client.KvMetadata;
import io.orchestrate.client.OrchestrateClient;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by amoli.ajarekar on 5/11/2015.
 */
public class OrchestrateAdapterTest {
    private Client client = OrchestrateClient.builder("dc8f355e-75c1-44aa-a2a1-f8e64a26254c")
            .host("https://api.ctl-uc1-a.orchestrate.io/v0/").build();

    private OrchestrateAdapter orchestrateAdapter = new OrchestrateAdapter();

    @Before
    public void setUp() throws Exception {
        orchestrateAdapter.setClient(client);
        client.deleteCollection("myobjects").get();
        client.close();
    }

    @Test
    public void save() {
        orchestrateAdapter.save("myobjects", "mykey", new MyObject("myname", "mydescription"));

        MyObject myObject = (this.client.kv("myobjects", "mykey").get(MyObject.class).get()).getValue();;
        assertThat(myObject.getName(), equalTo("myname"));
        assertThat(myObject.getDescription(), equalTo("mydescription"));
    }

    @Test
    public void findOne() {
        insertItem("myobjects", "mykey", "{`name`:`myname`, `description`:`mydescription`}");

        MyObject myObject = (MyObject) orchestrateAdapter.findOne("myobjects", "mykey", MyObject.class);
        assertThat(myObject.getName(), equalTo("myname"));
        assertThat(myObject.getDescription(), equalTo("mydescription"));
    }

    private KvMetadata insertItem(String collection, String key, String json_ish, Object...args) {
        return client.kv(collection, key)
                .put(String.format(json_ish.replace('`', '"'), args))
                .get();
    }
}
