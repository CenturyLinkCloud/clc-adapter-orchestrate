package com.ctlts.wfaas.data.orchestrate.query;

import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.repository.query.parser.Part;

import com.ctlts.wfaas.data.orchestrate.repository.EntityMetadata;

public class ExpressionCriteria extends Criteria {

    private Part part;
    private String value;
    private EntityMetadata metadata;
    
    public ExpressionCriteria(Criteria parent, EntityMetadata metadata, Part part, Object value) {
        super(parent);
        this.part = part;
        this.value = String.valueOf(value);
        this.metadata = metadata;
    }

    @Override
    public String createStatement() {
        
        StringBuffer realPath = new StringBuffer();
        PropertyPath path = part.getProperty();
        
        PropertyMetadata propertyMetadata = metadata.getPropertyMetadata(
                PropertyPath.from(path.getSegment(), path.getOwningType().getType()));
        
        realPath.append(propertyMetadata.getName());
        
        while(path.hasNext()) {
            
            path = path.next();
            
            propertyMetadata = propertyMetadata.getFieldMetadata().getPropertyMetadata(
                    PropertyPath.from(path.getSegment(), path.getOwningType().getType()));
            
            realPath.append(".").append(propertyMetadata.getName());
            
        }
        
        /*
         * TODO - Respect and JSON property name attributes when creating 
         * expression. @JSONProperty("alternate_name") for instance.
         */
        
        String query = String.format("%s:\"%s\"", realPath.toString(), value);
    
        return getContinuation() != null ? String.format(
                "%s %s", query, getContinuation().createQuery()) : query;
        
    }

}