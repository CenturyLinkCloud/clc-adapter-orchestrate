/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.query;

import java.util.Iterator;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

/**
 * @author mramach
 *
 */
public class OrchestrateQueryCreator extends AbstractQueryCreator<Query, Criteria> {

    public OrchestrateQueryCreator(PartTree tree, ParameterAccessor parameters) {
        super(tree, parameters);
    }

    @Override
    protected Criteria create(Part part, Iterator<Object> iterator) {
        return new ExpressionCriteria(part, iterator.next());
    }

    @Override
    protected Criteria and(Part part, Criteria base, Iterator<Object> iterator) {
        return base.and(part, iterator);
    }

    @Override
    protected Criteria or(Criteria base, Criteria criteria) {
        return new CompoundExpressionCriteria("OR", base, criteria);
    }

    @Override
    protected Query complete(Criteria criteria, Sort sort) {
        return criteria.createQuery();
    }
    
}
