/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.query;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.Parameters;

/**
 * @author mramach
 *
 */
public class OrchestrateQuery {
    
    private String statement;
    private Parameters<?,?> parameters;
    private Map<String, Object> parameterValues = new LinkedHashMap<String, Object>();
    
    public OrchestrateQuery(String statement, Parameters<?, ?> parameters) {
        this.statement = statement;
        this.parameters = parameters;
    }

    public void setParameter(int index, Object value) {
        
        Parameter p = parameters.getBindableParameter(index);
        parameterValues.put(p.getPlaceholder(), value);
        
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        
        String fullStatement = statement;
        
        for (Entry<String, Object> p : parameterValues.entrySet()) {
            
            while(fullStatement.indexOf(p.getKey()) >= 0) {
                fullStatement = fullStatement.replace(p.getKey(), String.valueOf(p.getValue()));
            }

        }

        return fullStatement;
        
    }

}
