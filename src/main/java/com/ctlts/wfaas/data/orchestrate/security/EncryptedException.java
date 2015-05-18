/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.security;

/**
 * @author mramach
 *
 */
public class EncryptedException extends RuntimeException {

    private static final long serialVersionUID = -8224425283859419985L;

    public EncryptedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncryptedException(String message) {
        super(message);
    }

}
