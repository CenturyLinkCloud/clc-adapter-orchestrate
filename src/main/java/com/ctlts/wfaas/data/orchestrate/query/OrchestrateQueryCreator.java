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

import com.ctlts.wfaas.data.orchestrate.repository.EntityMetadata;

/**
 * @author mramach
 *
 */
public class OrchestrateQueryCreator extends AbstractQueryCreator<Query, Criteria> {

    private EntityMetadata metadata;
    
    public OrchestrateQueryCreator(PartTree tree, EntityMetadata metadata, ParameterAccessor parameters) {
        super(tree, parameters);
        this.metadata = metadata;
    }

    @Override
    protected Criteria create(Part part, Iterator<Object> iterator) {
        return new ExpressionCriteria(null, metadata, part, iterator.next());
    }

    @Override
    protected Criteria and(Part part, Criteria base, Iterator<Object> iterator) {
        return base.and(base, metadata, part, iterator);
    }

    @Override
    protected Criteria or(Criteria base, Criteria criteria) {
        return new CompoundExpressionCriteria(base, "OR", base, criteria);
    }

    @Override
    protected Query complete(Criteria criteria, Sort sort) {
        return criteria.getRoot().createQuery();
    }
    
}
