package com.ctlts.wfaas.data.orchestrate.query;

import java.util.Iterator;

import org.springframework.data.repository.query.parser.Part;

public abstract class Criteria {
    
    private Criteria parent;
    private Continuation continuation;
    
    public Criteria(Criteria parent) {
        this.parent = parent;
    }

    public Criteria and(Criteria parent, Part p, Iterator<Object> iterator) {

        Criteria next = new ExpressionCriteria(parent, p, iterator.next());
        continuation = new Continuation("AND", next);
        
        return next;
        
    }
    
    public Continuation getContinuation() {
        return continuation;
    }
    
    public Criteria getParent() {
        return parent;
    }

    public Criteria getRoot() {

        if(getParent() == null) {
            return this;
        }
        
        Criteria root = getParent();
        
        while(root.getParent() != null) {
            root = root.getParent();
        }
        
        return root;
        
    }
    
    public abstract String createStatement();

}