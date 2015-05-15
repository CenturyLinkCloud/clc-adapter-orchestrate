/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;


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
        
        StringBuffer query = new StringBuffer();
        AtomicInteger orIdx = new AtomicInteger(-1);
        AtomicInteger idx = new AtomicInteger(-1);
        
        tree.forEach(p -> {
            
            if(orIdx.incrementAndGet() > 0) {
                query.append(" OR ");
            }
            
            StringBuffer expr = new StringBuffer();
            AtomicInteger exprIdx = new AtomicInteger(-1);
            
            p.forEach(c -> {
                
                if(exprIdx.incrementAndGet() > 0) {
                    expr.append(" AND ");
                }
                
                expr.append(eq(c, parameters[idx.incrementAndGet()]));
                
            });
            
            query.append("(").append(expr).append(")");
            
        });
        
        List<?> results = (List<?>) orchestrateTemplate.query(entityMetadata.getCollection(), 
                query.toString(), metadata.getDomainType());

        if(!queryMethod.isCollectionQuery()) {
            // TODO - Throw exception if we receive more than one value back in the result set.
            return results.stream().findFirst().orElse(null);
        }
        
        return results;
        
    }

    private String eq(Part part, Object value) {
        return String.format("%s:\"%s\"", part.getProperty().toDotPath(), String.valueOf(value));
    }
    
    @Override
    public QueryMethod getQueryMethod() {
        return queryMethod;
    }

}
