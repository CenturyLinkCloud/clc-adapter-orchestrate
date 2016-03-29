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

import java.util.Iterator;

import org.springframework.data.repository.query.parser.Part;

public abstract class Criteria {
    
    private Criteria parent;
    private Continuation continuation;
    
    public Criteria(Criteria parent) {
        this.parent = parent;
    }

    public Criteria and(Criteria parent, Part p, Iterator<Object> iterator) {

        Criteria next = ExpressionCriteriaFactory.create(parent, p, iterator.next());
        continuation = new Continuation("AND", next);
        
        return next;
        
    }
    
    public Continuation getContinuation() {
        return continuation;
    }
    
    public Criteria getParent() {
        return parent;
    }

    public Criteria getRoot() {

        if(getParent() == null) {
            return this;
        }
        
        Criteria root = getParent();
        
        while(root.getParent() != null) {
            root = root.getParent();
        }
        
        return root;
        
    }
    
    public abstract String createStatement();

}
