package com.ctlts.wfaas.data.orchestrate.query;

import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.repository.query.parser.Part;

import com.ctlts.wfaas.data.orchestrate.repository.EntityMetadata;

public class ExpressionCriteria extends Criteria {

    private Part part;
    private String value;
    
    public ExpressionCriteria(Criteria parent, Part part, Object value) {
        super(parent);
        this.part = part;
        this.value = String.valueOf(value);
    }

    @Override
    public String createStatement() {
        
        String query = String.format("%s:\"%s\"", resolveDotPath(), value);
    
        return getContinuation() != null ? String.format(
                "%s %s", query, getContinuation().createQuery()) : query;
        
    }

    private String resolveDotPath() {
        
        StringBuffer dotPath = new StringBuffer();
        
        part.getProperty().forEach(p -> {
            
            dotPath.append(getPropertyMetadata(p).getName()); 
            
            if(p.hasNext()){
                dotPath.append(".");
            }
            
        });
        
        return dotPath.toString();
        
    }
    
    private PropertyMetadata getPropertyMetadata(PropertyPath p) {
        
        return new EntityMetadata(p.getOwningType().getType())
            .getPropertyMetadata(PropertyPath.from(p.getSegment(), p.getOwningType().getType()));
        
    }

}