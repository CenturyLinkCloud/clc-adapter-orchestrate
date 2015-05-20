/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.query;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.Parameters;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.data.repository.query.parser.PartTree.OrPart;

/**
 * @author mramach
 *
 */
public class OrchestrateCriteriaBuilder {
    
    public static Criteria create(PartTree tree, Parameters<?,?> parameters) {
        
        AtomicInteger idx = new AtomicInteger(0);
        Criteria base = null;
        Criteria criteria = null;

        for(OrPart p : tree) {
            
            criteria = criteria == null ? OrchestrateCriteriaBuilder.create(p, idx, parameters) : 
                criteria.or(p, idx, parameters);

            if(base == null) {
                base = criteria;
            }
            
        }
        
        return base;
        
    }
    
    public static Criteria create(OrPart p, AtomicInteger idx, Parameters<?,?> parameters) {
        return new CompoundCriteria(p, idx, parameters);
    }
    
    public static Criteria create(Part p, AtomicInteger idx, Parameters<?,?> parameters) {
        return new ExpressionCriteria(p, idx, parameters);
    }
    
    public static abstract class Criteria {
        
        private Continuation continuation;
        private Parameters<?,?> parameters; 
        
        public Criteria(Parameters<?,?> parameters) {
            this.parameters = parameters;
        }
        
        public Criteria or(OrPart p, AtomicInteger idx, Parameters<?,?> parameters) {
            
            Criteria next = OrchestrateCriteriaBuilder.create(p, idx, parameters);
            continuation = new Continuation("OR", next);
            
            return next;
            
        }

        public Criteria and(Part p, AtomicInteger idx, Parameters<?,?> parameters) {
            
            Criteria next = OrchestrateCriteriaBuilder.create(p, idx, parameters);
            continuation = new Continuation("AND", next);
            
            return next;
            
        }
        
        public Continuation getContinuation() {
            return continuation;
        }
        
        public OrchestrateQuery createQuery() {
            return new OrchestrateQuery(createStatement(), parameters);
        }
        
        public abstract String createStatement();

    }
    
    public static class CompoundCriteria extends Criteria {
        
        private Criteria firstChild = null;

        public CompoundCriteria(OrPart p, AtomicInteger idx, Parameters<?,?> parameters) {
            
            super(parameters);
            
            Criteria child = null;
            
            for(Part cp : p) {
                
                child = child == null ? OrchestrateCriteriaBuilder.create(cp, idx, parameters) : 
                    child.and(cp, idx, parameters);
            
                if(firstChild == null) {
                    firstChild = child;
                }
                
            }
            
        }

        @Override
        public String createStatement() {
            
            String query = String.format("(%s)", firstChild.createStatement());
            
            return getContinuation() != null ? String.format(
                    "%s %s", query, getContinuation().createQuery()) : query;
                    
        }
        
    }

    public static class ExpressionCriteria extends Criteria {

        private Part part;
        private Parameter parameter;
        
        public ExpressionCriteria(Part p, AtomicInteger idx, Parameters<?,?> parameters) {
            super(parameters);
            this.part = p;
            this.parameter = parameters.getBindableParameter(idx.getAndIncrement());
        }
        
        @Override
        public String createStatement() {
            
            String query = String.format("%s:\"%s\"", part.getProperty().toDotPath(),
                    parameter.getPlaceholder());
        
            return getContinuation() != null ? String.format(
                    "%s %s", query, getContinuation().createQuery()) : query;
            
        }

    }
    
    private static class Continuation {
        
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
    
}
