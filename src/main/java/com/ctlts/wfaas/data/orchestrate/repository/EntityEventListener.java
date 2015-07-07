/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

/**
 * @author mramach
 *
 */
public interface EntityEventListener {
    
    void onEvent(Object entity);

}
