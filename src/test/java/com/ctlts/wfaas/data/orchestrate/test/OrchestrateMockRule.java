/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.*;
import static com.github.tomakehurst.wiremock.http.HttpHeader.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

/**
 * @author mramach
 *
 */
public class OrchestrateMockRule extends WireMockRule {
    
    public OrchestrateMockRule() {

        super(wireMockConfig().port(8080).extensions(new OrchestrateResponseTransformer()));

        stubFor(put(urlMatching("/v0/.*/.*")).willReturn(aResponse()));
        
    }
    
    public static class OrchestrateResponseTransformer extends ResponseTransformer {
        
        // (collection_id) -> { key -> value}
        private Map<String, Map<String, String>> collections = new LinkedHashMap<String, Map<String,String>>();
        
        @Override
        public String name() {
            return "orchestrate-response-transformer";
        }

        @Override
        public ResponseDefinition transform(Request request,
                ResponseDefinition responseDefinition, FileSource files) {

            Method handler = Arrays.asList(getClass().getDeclaredMethods()).stream().filter(m -> {
                
                Path p = m.getAnnotation(Path.class);
                
                return p == null ? false : ((Pattern.compile(p.value()).matcher(request.getUrl()).matches()) 
                        && p.method().equals(request.getMethod()));
                
            }).findAny().orElseThrow(() -> new RuntimeException("No path found for request " + request.getUrl()));
            
            try {
                
                return (ResponseDefinition) handler.invoke(this, request, responseDefinition);
                
            } catch (Exception e) {
                return ResponseDefinition.notFound();
            }
            
        }

        @Path(value = ".*/v0/(?<collection>.*)/(?<key>.*)", method = RequestMethod.PUT)
        private ResponseDefinition put(Request request, ResponseDefinition responseDefinition) {
            
            Matcher matcher = Pattern.compile(".*/v0/(?<collection>.*)/(?<key>.*)").matcher(request.getUrl());
            matcher.find();
            String collection = matcher.group("collection");
            String key = matcher.group("key");
            
            collections.putIfAbsent(collection, new LinkedHashMap<String, String>());
            collections.get(collection).put(key, request.getBodyAsString());
            
            HttpHeaders headers = new HttpHeaders().plus(
                    httpHeader("X-ORCHESTRATE-REQ-ID:", UUID.randomUUID().toString()),
                    httpHeader("ETag:", UUID.randomUUID().toString())
            );
            
            ResponseDefinition def = ResponseDefinition.ok();
            def.setStatus(HttpStatus.SC_CREATED);
            def.setHeaders(headers);
            def.setBody("");
            
            return def;
            
        }
        
        @Path(value = ".*/v0/(?<collection>.*)/(?<key>.*)", method = RequestMethod.GET)
        private ResponseDefinition get(Request request, ResponseDefinition responseDefinition) {
            
            Matcher matcher = Pattern.compile(".*/v0/(?<collection>.*)/(?<key>.*)").matcher(request.getUrl());
            matcher.find();
            String collection = matcher.group("collection");
            String key = matcher.group("key");
            
            if(!collections.containsKey(collection)) {
                return ResponseDefinition.notFound();
            }
            
            if(!collections.get(collection).containsKey(key)) {
                return ResponseDefinition.notFound();
            }
            
            HttpHeaders headers = new HttpHeaders().plus(
                    httpHeader("X-ORCHESTRATE-REQ-ID:", UUID.randomUUID().toString()),
                    httpHeader("ETag:", UUID.randomUUID().toString())
            );
            
            ResponseDefinition def = ResponseDefinition.ok();
            def.setStatus(HttpStatus.SC_CREATED);
            def.setHeaders(headers);
            def.setBody(collections.get(collection).get(key));
            
            return def;
            
        }
        
    }
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Path {
        
        RequestMethod method() default RequestMethod.GET;
        
        String value();
        
    }

}
