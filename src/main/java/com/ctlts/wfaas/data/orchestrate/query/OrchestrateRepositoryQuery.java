/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.query;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.PartTree;

import com.ctlts.wfaas.data.orchestrate.query.OrchestrateCriteriaBuilder.Criteria;
import com.ctlts.wfaas.data.orchestrate.repository.EntityMetadata;
import com.ctlts.wfaas.data.orchestrate.repository.OrchestrateTemplate;


/**
 * @author mramach
 *
 */
public class OrchestrateRepositoryQuery implements RepositoryQuery {
    
    private QueryMethod queryMethod;
    private PartTree tree;
    private RepositoryMetadata metadata;
    private EntityMetadata entityMetadata;
    private OrchestrateTemplate orchestrateTemplate;

    public OrchestrateRepositoryQuery(Method method, RepositoryMetadata metadata, OrchestrateTemplate orchestrateTemplate) {
        this.queryMethod = new QueryMethod(method, metadata);
        this.tree = new PartTree(method.getName(), queryMethod.getEntityInformation().getJavaType());
        this.metadata = metadata;
        this.entityMetadata = new EntityMetadata(metadata.getDomainType());
        this.orchestrateTemplate = orchestrateTemplate;
    }

    @Override
    public Object execute(Object[] parameters) {

        if(tree.isDistinct()) {
            throw new UnsupportedOperationException("Use of Distinct in dynamic queries is not supported.");
        }
        
        if(tree.isLimiting()) {
            throw new UnsupportedOperationException("Use of limiting in dynamic queries is not supported.");
        }
        
        if(tree.isCountProjection()) {
            throw new UnsupportedOperationException("Use of Count in dynamic queries is not supported.");
        }
        
        if(tree.isDelete()) {
            throw new UnsupportedOperationException("Use of Delete or Remove in dynamic queries is not supported.");
        }

        if(queryMethod.isSliceQuery()) {
            throw new UnsupportedOperationException("Slice in dynamic queries is not supported.");
        }
        
        if(queryMethod.isPageQuery()) {
            throw new UnsupportedOperationException("Paging in dynamic queries is not supported.");
        }

        if(tree.getSort() != null) {
            throw new UnsupportedOperationException("Order By in dynamic queries is not supported.");
        }
        
        Criteria criteria = OrchestrateCriteriaBuilder.create(tree, parameters);
        
        List<?> results = (List<?>) orchestrateTemplate.query(entityMetadata.getCollection(), 
                criteria.createQuery(), metadata.getDomainType());

        if(!queryMethod.isCollectionQuery()) {
            // TODO - Throw exception if we receive more than one value back in the result set.
            return results.stream().findFirst().orElse(null);
        }
        
        return results;
        
    }
    
    @Override
    public QueryMethod getQueryMethod() {
        return queryMethod;
    }

}
