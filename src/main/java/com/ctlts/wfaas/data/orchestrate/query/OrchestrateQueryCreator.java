/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.query;

import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mapping.PropertyPath;
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
    
    public OrchestrateQueryCreator(EntityMetadata metadata, PartTree tree, ParameterAccessor parameters) {
        super(tree, parameters);
        this.metadata = metadata;
    }

    @Override
    protected Criteria create(Part part, Iterator<Object> iterator) {
        return new ExpressionCriteria(null, part, iterator.next());
    }

    @Override
    protected Criteria and(Part part, Criteria base, Iterator<Object> iterator) {
        return base.and(base, part, iterator);
    }

    @Override
    protected Criteria or(Criteria base, Criteria criteria) {
        return new CompoundExpressionCriteria(base, "OR", base, criteria);
    }

    @Override
    protected Query complete(Criteria criteria, Sort sort) {
        return new Query(criteria.getRoot().createStatement(), createSort(sort));
    }
    
    private String createSort(Sort sort) {
        
        if(sort == null) {
            return "";
        }
        
        return StreamSupport.stream(sort.spliterator(), false)
                .map(this::createSortExpression).collect(Collectors.joining(","));
        
    }
    
    private String createSortExpression(Order order) {
        
        PropertyPath path = PropertyPath.from(order.getProperty(), metadata.getType());
        
        return new StringBuffer(StreamSupport.stream(path.spliterator(), false)
                .map(p -> getPropertyMetadata(p).getName())
                        .collect(Collectors.joining(".")))
                                .append(":").append(order.getDirection().name()).toString();
        
    }
    
    private PropertyMetadata getPropertyMetadata(PropertyPath p) {
        
        return new EntityMetadata(p.getOwningType().getType())
            .getPropertyMetadata(PropertyPath.from(p.getSegment(), p.getOwningType().getType()));
        
    }
    
}
