package com.ctlts.wfaas.adapter.orchestrate;

/**
 * Created by amoli.ajarekar on 5/11/2015.
 */
public class MyObject {

    private String name;

    private String description;

    public MyObject() {
    }

    public MyObject(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
