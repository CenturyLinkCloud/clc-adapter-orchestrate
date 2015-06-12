/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.query;

/**
 * @author mramach
 *
 */
public class CompoundExpressionCriteria extends Criteria {

    private String conjunction;
    private Criteria base;
    private Criteria criteria;
    
    public CompoundExpressionCriteria(Criteria parent, String conjunction, Criteria base, Criteria criteria) {
        super(parent);
        this.conjunction = conjunction;
        this.base = base;
        this.criteria = criteria;
    }

    @Override
    public String createStatement() {
        return String.format("%s %s %s", base.createStatement(), conjunction, criteria.createStatement());
    }

}
