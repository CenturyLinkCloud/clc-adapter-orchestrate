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

import java.io.Serializable;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author mramach
 *
 */
public interface OrchestrateRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

    /**
     * Conditional save operation based on the provided ref value. If the save operation fails
     * because the ref is out of date, an http status code of 412, precondition failed is thrown.
     * 
     * @param entity The entity to save.
     * @param ref The previous ref value.
     * @return The newly saved entity.
     */
    <S extends T> S save(S entity, String ref);
    
    /**
     * Find method that returns a ref field indicating the current version of the resource.
     * 
     * @param id The identifier of the entity.
     * @return Entity containing the ref and deserialized value.
     */
    Entity<T> find(ID id);
    
}
