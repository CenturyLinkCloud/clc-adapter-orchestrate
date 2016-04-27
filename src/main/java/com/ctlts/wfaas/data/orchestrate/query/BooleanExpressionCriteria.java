/**
* Copyright 2015 CenturyLink
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*    http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.ctlts.wfaas.data.orchestrate.query;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.repository.query.parser.Part;

import com.ctlts.wfaas.data.orchestrate.repository.EntityMetadata;

public class BooleanExpressionCriteria extends Criteria {

    private Part part;
    private String value;
    
    public BooleanExpressionCriteria(Criteria parent, Part part, Object value) {
        super(parent);
        this.part = part;
        this.value = String.valueOf(value);
    }

    @Override
    public String createStatement() {
        
        // Protecting against query injection.
        String escaped = String.valueOf(value)
            .replaceAll("[\\`]", "\\\\`");
        
        String query = String.format("value.%s:%s", resolveDotPath(), escaped);
    
        return getContinuation() != null ? String.format(
                "%s %s", query, getContinuation().createQuery()) : query;
        
    }

    private String resolveDotPath() {
        
        return StreamSupport.stream(part.getProperty().spliterator(), false)
            .map(p -> getPropertyMetadata(p).getName())
                .collect(Collectors.joining("."));
        
    }
    
    private PropertyMetadata getPropertyMetadata(PropertyPath p) {
        
        return new EntityMetadata(p.getOwningType().getType())
            .getPropertyMetadata(PropertyPath.from(p.getSegment(), p.getOwningType().getType()));
        
    }

}
