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
    private String sort;
    
    public Query(String statement, String sort) {
        this.statement = statement;
        this.sort = sort;
    }
    
    public String getStatement() {
        return statement;
    }

    public String getSort() {
        return sort;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return statement;
    }

}
