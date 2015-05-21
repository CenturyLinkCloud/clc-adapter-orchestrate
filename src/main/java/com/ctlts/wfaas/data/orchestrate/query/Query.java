/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.query;


/**
 * @author mramach
 *
 */
public class Query {
    
    private String statement;
    
    public Query(String statement) {
        this.statement = statement;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return statement;
    }

}
