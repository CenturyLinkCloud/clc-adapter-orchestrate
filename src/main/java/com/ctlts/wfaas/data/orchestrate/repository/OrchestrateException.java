/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.repository;

/**
 * @author mramach
 *
 */
public class OrchestrateException extends RuntimeException {

    private static final long serialVersionUID = 2158649350588517116L;

    public OrchestrateException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrchestrateException(String message) {
        super(message);
    }
    
}
