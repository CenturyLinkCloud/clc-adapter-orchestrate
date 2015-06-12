package com.ctlts.wfaas.data.orchestrate.query;

import org.springframework.data.repository.query.parser.Part;

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
        
        String query = String.format("%s:\"%s\"", part.getProperty().toDotPath(), value);
    
        return getContinuation() != null ? String.format(
                "%s %s", query, getContinuation().createQuery()) : query;
        
    }

}