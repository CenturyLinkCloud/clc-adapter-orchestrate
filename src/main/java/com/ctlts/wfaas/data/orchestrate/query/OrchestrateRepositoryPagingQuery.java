/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.query;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.core.RepositoryMetadata;

import com.ctlts.wfaas.data.orchestrate.repository.OrchestrateTemplate;
import com.ctlts.wfaas.data.orchestrate.repository.ResultSet;

/**
 * @author mramach
 *
 */
public class OrchestrateRepositoryPagingQuery extends OrchestrateRepositoryQuery {

    public OrchestrateRepositoryPagingQuery(Method method, RepositoryMetadata metadata, 
            OrchestrateTemplate orchestrateTemplate) {

        super(method, metadata, orchestrateTemplate);
        
    }

    @Override
    protected Object execute(Query query, Object[] parameters) {
        
        PageRequest pageReq = (PageRequest) parameters[getQueryMethod().getParameters().getPageableIndex()];
        
        ResultSet<?> results = getOrchestrateTemplate().query(getEntityMetadata().getCollection(), query, 
                getMetadata().getDomainType(), pageReq.getPageSize(), pageReq.getPageSize() * pageReq.getOffset());
        
        return new PageImpl<>((List<?>) results.getValue(), pageReq, results.getTotalSize());
        
    }

}
