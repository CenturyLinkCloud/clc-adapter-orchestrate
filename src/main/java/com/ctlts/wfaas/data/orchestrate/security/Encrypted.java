/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Marks a field as encrypted. When {@link EncryptedSerializer} and {@link EncryptedDeserializer}
 * are registered with the {@link ObjectMapper}, this will result in the value being processed by 
 * the custom serializer. This requires that an instance of {@link EncryptedContext} is available
 * on the current execution thread.
 * 
 * @author mramach
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Encrypted {

}
