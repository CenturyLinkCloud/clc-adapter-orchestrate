/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.query;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.PartTree;

import com.ctlts.wfaas.data.orchestrate.repository.EntityMetadata;
import com.ctlts.wfaas.data.orchestrate.repository.OrchestrateTemplate;
import com.ctlts.wfaas.data.orchestrate.repository.ResultSet;


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
        
        if(tree.isDelete()) {
            throw new UnsupportedOperationException("Use of Delete or Remove in dynamic queries is not supported.");
        }

        if(tree.getSort() != null) {
            throw new UnsupportedOperationException("Order By in dynamic queries is not supported.");
        }
        
        Query query = new OrchestrateQueryCreator(tree, new ParametersParameterAccessor(
                queryMethod.getParameters(), parameters)).createQuery();
        
        return execute(query, parameters, getMaxResultsSize());
        
    }

    protected Object execute(Query query, Object[] parameters, int maxResults) {
        
        ResultSet<?> results = orchestrateTemplate.query(entityMetadata.getCollection(), 
                query, metadata.getDomainType(), maxResults, 0);
            
        if(tree.isCountProjection()) {
            return results.getTotalSize();
        }
        
        if(!queryMethod.isCollectionQuery()) {
            return ((List<?>)results.getValue()).stream().findFirst().orElse(null);
        }
        
        return results.getValue();
        
    }

    private int getMaxResultsSize() {

        // TODO - Throw exception when requested max results exceeds template max results.
        
        if(tree.isCountProjection()) {
            return 0;
        }
        
        return tree.isLimiting() ? tree.getMaxResults() : orchestrateTemplate.getMaxResults();
        
    }
    
    @Override
    public QueryMethod getQueryMethod() {
        return queryMethod;
    }

    protected RepositoryMetadata getMetadata() {
        return metadata;
    }

    protected EntityMetadata getEntityMetadata() {
        return entityMetadata;
    }

    protected OrchestrateTemplate getOrchestrateTemplate() {
        return orchestrateTemplate;
    }

}
