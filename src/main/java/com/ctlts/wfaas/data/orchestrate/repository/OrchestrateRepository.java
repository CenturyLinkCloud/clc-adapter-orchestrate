/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

/**
 * @author mramach
 *
 */
public interface OrchestrateRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {

}
