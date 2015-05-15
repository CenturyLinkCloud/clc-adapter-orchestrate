/**
 * 
 */
package com.ctlts.wfaas.data.orchestrate.test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.*;
import static com.github.tomakehurst.wiremock.http.HttpHeader.*;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.RAMDirectory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.QueryParameter;
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
        
        private static final String VAR_VALUE = "_value";
        private static final String VAR_ID = "_id";
        private static final String VAR_COLLECTION = "_collection";
        
        private RAMDirectory dir = new RAMDirectory();
        
        @Override
        public String name() {
            return "orchestrate-response-transformer";
        }

        @Override
        public ResponseDefinition transform(Request request,
                ResponseDefinition responseDefinition, FileSource files) {

            Method handler = Arrays.asList(getClass().getDeclaredMethods()).stream()
                    .filter(m -> m.getAnnotation(Path.class) != null)
                    .sorted((o1, o2) -> Integer.compare(o1.getAnnotation(Path.class).precedence(), o2.getAnnotation(Path.class).precedence()))
                    .filter(m -> {
                        
                        Path p = m.getAnnotation(Path.class);

                        boolean result = Arrays.stream(p.parameters()).allMatch(qp -> qp.required() ? request.queryParameter(qp.value()).isPresent() : true);
                        
                        return ((Pattern.compile(p.value()).matcher(request.getUrl()).matches()) 
                                && p.method().equals(request.getMethod()))
                                    && result;
                
            }).findFirst().orElseThrow(() -> new RuntimeException("No path found for request " + request.getUrl()));
            
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
            
            try {
                
                IndexWriter index = new IndexWriter(dir, 
                        new IndexWriterConfig(new StandardAnalyzer()));
                
                index.addDocument(createDocument(collection, key, request.getBodyAsString()));
                index.close();
                
            } catch (IOException e) {
                return new ResponseDefinition(HttpStatus.SC_INTERNAL_SERVER_ERROR, "");
            }
            
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
        
        @Path(value = ".*/v0/(?<collection>.*)/(?<key>.*)", method = RequestMethod.GET, precedence = 1)
        private ResponseDefinition get(Request request, ResponseDefinition responseDefinition) {
            
            try {
                
                Matcher matcher = Pattern.compile(".*/v0/(?<collection>.*)/(?<key>.*)").matcher(request.getUrl());
                matcher.find();
//                String collection = matcher.group("collection");
                String key = matcher.group("key");
                
                IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
                
                TopScoreDocCollector collector = TopScoreDocCollector.create(10);
                searcher.search(new TermQuery(new Term(VAR_ID, key)), collector);

                if(collector.getTotalHits() == 0) {
                    return ResponseDefinition.notFound();
                }
                
                ScoreDoc hit = collector.topDocs().scoreDocs[0];
                Document doc = searcher.doc(hit.doc);
                
                String value = doc.get(VAR_VALUE);
                
                HttpHeaders headers = new HttpHeaders().plus(
                        httpHeader("X-ORCHESTRATE-REQ-ID:", UUID.randomUUID().toString()),
                        httpHeader("ETag:", UUID.randomUUID().toString())
                );
                
                ResponseDefinition def = ResponseDefinition.ok();
                def.setStatus(HttpStatus.SC_CREATED);
                def.setHeaders(headers);
                def.setBody(value);
                
                return def;
                
            } catch (IOException e) {
                return new ResponseDefinition(HttpStatus.SC_INTERNAL_SERVER_ERROR, "");
            }
            
        }
        
        @Path(value = ".*/v0/(?<collection>.*)\\?.*", method = RequestMethod.GET, precedence = 0, parameters = {
                @QueryParam(value = "query")
        })
        private ResponseDefinition query(Request request, ResponseDefinition responseDefinition) {
            
            try {
                
                Matcher matcher = Pattern.compile(".*/v0/(?<collection>.*)\\?.*").matcher(request.getUrl());
                matcher.find();
//                String collection = matcher.group("collection");
                QueryParameter queryParam = request.queryParameter("query");
                
                IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));
                Analyzer analyzer = new StandardAnalyzer();
                TopScoreDocCollector collector = TopScoreDocCollector.create(10);
                QueryParser parser = new QueryParser(VAR_VALUE, analyzer);
                
                String queryString = URLDecoder.decode(queryParam.firstValue(), "UTF-8");
                
                Query query = parser.parse(queryString);
                
                searcher.search(query, collector);

                if(collector.getTotalHits() == 0) {
                    return ResponseDefinition.notFound();
                }
                
                // Construct response body
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> response = new LinkedHashMap<String, Object>();
                List<Map<String, Object>> results = new LinkedList<Map<String,Object>>();
                Long reftime = System.currentTimeMillis() / 1000;
                
                response.put("count", 1);
                response.put("total_count", 1);
                response.put("results", results);
                
                Arrays.stream(collector.topDocs().scoreDocs).forEach(hit -> {
                    
                    try {
                        
                        Document doc = searcher.doc(hit.doc);
                        String key = doc.get(VAR_ID);
                        String col = doc.get(VAR_COLLECTION);

                        Map<String, Object> result = new LinkedHashMap<String, Object>();
                        Map<String, Object> path = new LinkedHashMap<String, Object>();
                        
                        path.put("collection", col);
                        path.put("kind", "item");
                        path.put("key", key);
                        path.put("ref", UUID.randomUUID().toString());
                        path.put("reftime", reftime);
                        
                        result.put("path", path);
                        result.put("value", mapper.readValue(doc.get(VAR_VALUE), Map.class));
                        result.put("score", 10);
                        result.put("reftime", reftime);
                        
                        results.add(result);
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                });
                
                HttpHeaders headers = new HttpHeaders().plus(
                        httpHeader("X-ORCHESTRATE-REQ-ID:", UUID.randomUUID().toString()),
                        httpHeader("ETag:", UUID.randomUUID().toString()),
                        httpHeader("Content-Type:", "application-json")
                );
                
                ResponseDefinition def = ResponseDefinition.ok();
                def.setHeaders(headers);
                def.setBody(mapper.writeValueAsString(response));
                
                return def;
                
            } catch (Exception e) {
                return new ResponseDefinition(HttpStatus.SC_INTERNAL_SERVER_ERROR, "");
            }
            
        }
        
        private Document createDocument(String collection, String id, String value) {
            
            Document doc = new Document();
            doc.add(new StringField(VAR_COLLECTION, collection, Field.Store.YES));
            doc.add(new StringField(VAR_ID, id, Field.Store.YES));
            doc.add(new StringField(VAR_VALUE, value, Field.Store.YES));
            
            try {
                
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readValue(value, JsonNode.class);
                
                root.fields().forEachRemaining(e -> {
                    doc.add(new TextField(e.getKey(), e.getValue().asText(), Field.Store.YES));
                });
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return doc;
            
        }
        
    }
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Path {
        
        RequestMethod method() default RequestMethod.GET;
        
        String value();
        
        int precedence() default 0;
        
        QueryParam[] parameters() default {};
        
    }
    
    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface QueryParam {
        
        String value();
        
        boolean required() default true;
        
    }

}
