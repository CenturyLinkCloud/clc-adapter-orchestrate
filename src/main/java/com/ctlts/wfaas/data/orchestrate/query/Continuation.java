/**
*
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

class Continuation {
    
    private String conjunction;
    private Criteria next;
    
    public Continuation(String conjunction, Criteria next) {
        this.conjunction = conjunction;
        this.next = next;
    }
    
    public String createQuery() {
        return String.format("%s %s", conjunction, next.createStatement());
    }
    
}
