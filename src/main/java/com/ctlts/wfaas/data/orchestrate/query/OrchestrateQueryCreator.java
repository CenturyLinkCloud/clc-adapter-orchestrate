/**
 * 
* Copyright 2015 CenturyLink
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*    http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
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
        return new Query(criteria.getRoot().createStatement(), createSort(metadata, sort));
    }
    
    public static String createSort(EntityMetadata metadata, Sort sort) {
        
        if(sort == null) {
            return "";
        }
        
        return StreamSupport.stream(sort.spliterator(), false)
                .map(o -> OrchestrateQueryCreator.createSortExpression(metadata, o))
                        .collect(Collectors.joining(","));
        
    }
    
    private static String createSortExpression(EntityMetadata metadata, Order order) {
        
        PropertyPath path = PropertyPath.from(order.getProperty(), metadata.getType());
        
        return new StringBuffer(StreamSupport.stream(path.spliterator(), false)
                .map(p -> getPropertyMetadata(p).getName())
                        .collect(Collectors.joining(".")))
                                .append(":").append(order.getDirection().name()).toString();
        
    }
    
    private static PropertyMetadata getPropertyMetadata(PropertyPath p) {
        
        return new EntityMetadata(p.getOwningType().getType())
            .getPropertyMetadata(PropertyPath.from(p.getSegment(), p.getOwningType().getType()));
        
    }
    
}
