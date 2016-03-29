/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.query;

import org.springframework.data.repository.query.parser.Part;

/**
 * @author mramach
 *
 */
public class ExpressionCriteriaFactory {
    
    public static Criteria create(Criteria parent, Part part, Object value) {
        
        if(value instanceof Boolean) {
            return new BooleanExpressionCriteria(parent, part, value);
        }
        
        return new StringExpressionCriteria(parent, part, value);
        
    }

}
