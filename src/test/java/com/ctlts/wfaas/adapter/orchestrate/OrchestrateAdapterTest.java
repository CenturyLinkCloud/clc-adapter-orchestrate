package com.ctlts.wfaas.adapter.orchestrate;

import io.orchestrate.client.Client;
import io.orchestrate.client.OrchestrateClient;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by amoli.ajarekar on 5/11/2015.
 */
public class OrchestrateAdapterTest {
    private Client client = OrchestrateClient.builder("dc8f355e-75c1-44aa-a2a1-f8e64a26254c")
            .host("https://api.ctl-uc1-a.orchestrate.io/v0/").build();

    private OrchestrateAdapter orchestrateAdapter = new OrchestrateAdapter(client);

    @Before
    public void setUp() throws Exception {
        client.deleteCollection("myobjects").get();
    }

    @Test
    public void save() {
        orchestrateAdapter.save("myobjects", "mykey", new MyObject("myname", "mydescription"));

        MyObject myObject = (MyObject) orchestrateAdapter.findOne("myobjects", "mykey", MyObject.class);
        assertThat(myObject.getName(), equalTo("myname"));
        assertThat(myObject.getDescription(), equalTo("mydescription"));
    }
}
