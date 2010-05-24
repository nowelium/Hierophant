package hierophant.core.hash.impl;

import hierophant.core.hash.Circle;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

public class SortedCircle<E> implements Circle<E> {
    
    private final SortedMap<Long, E> values;

    public SortedCircle(){
        this(new TreeMap<Long, E>());
    }
    
    public SortedCircle(final SortedMap<Long, E> values){
        this.values = Collections.synchronizedSortedMap(values);
    }
    
    public boolean containsKey(Long key) {
        return values.containsKey(key);
    }

    public Long firstKey() {
        return values.firstKey();
    }

    public E get(Long key) {
        return values.get(key);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public void put(Long key, E e) {
        values.put(key, e);
    }
    
    public void remove(Long key){
        values.remove(key);
    }

    public Circle<E> tail(Long key) {
        return new SortedCircle<E>(values.tailMap(key));
    }

}
