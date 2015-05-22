package com.ctlts.wfaas.data.orchestrate.query;

class Continuation {
    
    private String conjunction;
    private Criteria next;
    
    public Continuation(String conjunction, Criteria next) {
        this.conjunction = conjunction;
        this.next = next;
    }
    
    public String createQuery() {
        return String.format("%s %s", conjunction, next.createStatement());
    }
    
}