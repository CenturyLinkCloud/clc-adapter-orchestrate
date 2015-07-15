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
package com.ctlts.wfaas.data.orchestrate.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

/**
 * @author mramach
 *
 */
public interface TestEntityQueryDslRespository extends OrchestrateRepository<TestEntity, String> {
    
    public TestEntity findById(String id);

    public List<TestEntity> findByStringProperty(String stringProperty);

    public TestEntity findByObjectProperty_StringProperty(String stringProperty);

    public TestEntity findByStringPropertyAndObjectProperty_StringProperty(
            String stringProperty, String nestedStringProeprty);
    
    public TestEntity findByStringPropertyOrObjectProperty_StringProperty(
            String stringProperty, String nestedStringProeprty);

    public TestEntity findDistinctByStringProperty(String stringProperty);

    public List<TestEntity> findFirst1ByStringProperty(String stringProperty);

    public Integer countByStringProperty(String stringProperty);

    public void deleteByStringProperty(String stringProperty);

    public Page<TestEntity> findByStringProperty(String stringProperty, Pageable req);

    public Slice<TestEntity> findByStringProperty2(String stringProperty2, Pageable req);

    public List<TestEntity> findByStringPropertyOrderByStringProperty2Asc(String stringProperty);
    
    public List<TestEntity> findByStringProperty(String stringProperty, Sort sort);
    
}
