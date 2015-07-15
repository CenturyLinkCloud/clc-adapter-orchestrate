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

/**
 * @author mramach
 *
 */
public class CompoundExpressionCriteria extends Criteria {

    private String conjunction;
    private Criteria base;
    private Criteria criteria;
    
    public CompoundExpressionCriteria(Criteria parent, String conjunction, Criteria base, Criteria criteria) {
        super(parent);
        this.conjunction = conjunction;
        this.base = base;
        this.criteria = criteria;
    }

    @Override
    public String createStatement() {
        return String.format("%s %s %s", base.createStatement(), conjunction, criteria.createStatement());
    }

}
