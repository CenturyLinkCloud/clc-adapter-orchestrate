/**
 * 
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
