package com.ctlts.wfaas.data.orchestrate.query;

import java.util.Iterator;

import org.springframework.data.repository.query.parser.Part;

public abstract class Criteria {
    
    private Continuation continuation;

    public Criteria and(Part p, Iterator<Object> iterator) {

        Criteria next = new ExpressionCriteria(p, iterator.next());
        continuation = new Continuation("AND", next);
        
        return next;
        
    }
    
    public Continuation getContinuation() {
        return continuation;
    }
    
    public Query createQuery() {
        return new Query(createStatement());
    }
    
    public abstract String createStatement();

}