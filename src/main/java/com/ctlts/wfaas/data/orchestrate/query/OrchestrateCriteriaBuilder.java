/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.query;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.data.repository.query.parser.PartTree.OrPart;

/**
 * @author mramach
 *
 */
public class OrchestrateCriteriaBuilder {
    
    public static Criteria create(PartTree tree, Object[] parameters) {
        
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
    
    public static Criteria create(OrPart p, AtomicInteger idx, Object[] parameters) {
        return new CompoundCriteria(p, idx, parameters);
    }
    
    public static Criteria create(Part p, AtomicInteger idx, Object[] parameters) {
        return new ExpressionCriteria(p, idx, parameters);
    }
    
    public static abstract class Criteria {
        
        private Continuation continuation;
        
        public Criteria or(OrPart p, AtomicInteger idx, Object[] parameters) {
            
            Criteria next = OrchestrateCriteriaBuilder.create(p, idx, parameters);
            continuation = new Continuation("OR", next);
            
            return next;
            
        }

        public Criteria and(Part p, AtomicInteger idx, Object[] parameters) {
            
            Criteria next = OrchestrateCriteriaBuilder.create(p, idx, parameters);
            continuation = new Continuation("AND", next);
            
            return next;
            
        }
        
        public Continuation getContinuation() {
            return continuation;
        }

        public abstract String createQuery();

    }
    
    public static class CompoundCriteria extends Criteria {
        
        private Criteria firstChild = null;

        public CompoundCriteria(OrPart p, AtomicInteger idx, Object[] parameters) {
            
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
        public String createQuery() {
            
            String query = String.format("(%s)", firstChild.createQuery());
            
            return getContinuation() != null ? String.format(
                    "%s %s", query, getContinuation().createQuery()) : query;
                    
        }
        
    }

    public static class ExpressionCriteria extends Criteria {

        private Part part;
        private Object parameter;
        
        public ExpressionCriteria(Part p, AtomicInteger idx, Object[] parameters) {
            this.part = p;
            this.parameter = parameters[idx.getAndIncrement()];
        }
        
        @Override
        public String createQuery() {
            
            String query = String.format("%s:\"%s\"", part.getProperty().toDotPath(), parameter);
        
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
            return String.format("%s %s", conjunction, next.createQuery());
        }
        
    }
    
}
