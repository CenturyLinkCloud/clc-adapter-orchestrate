/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.lang.reflect.Method;
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
        
        AtomicInteger idx = new AtomicInteger(-1);
        StringBuffer query = new StringBuffer();
        
        tree.getParts().forEach(p -> {
            query.append(eq(p, parameters[idx.incrementAndGet()]));
        });
        
        Object result = orchestrateTemplate.query(entityMetadata.getCollection(), 
                query.toString(), metadata.getDomainType());

        return result;
        
    }

    private String eq(Part part, Object value) {
        return String.format("%s:\"%s\"", part.getProperty().toDotPath(), String.valueOf(value));
    }
    
    @Override
    public QueryMethod getQueryMethod() {
        return queryMethod;
    }

}
