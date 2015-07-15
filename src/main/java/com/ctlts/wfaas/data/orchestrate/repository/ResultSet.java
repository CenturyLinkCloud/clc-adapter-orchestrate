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

/**
 * @author mramach
 *
 */
public class ResultSet<T> {

    private T value;
    private int size;
    private int totalSize;
    
    public ResultSet(T value, int size, int totalSize) {
        this.value = value;
        this.size = size;
        this.totalSize = totalSize;
    }

    public T getValue() {
        return value;
    }

    public int getSize() {
        return size;
    }

    public int getTotalSize() {
        return totalSize;
    }
    
}
