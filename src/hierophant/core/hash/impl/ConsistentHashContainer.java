package hierophant.core.hash.impl;

import hierophant.core.hash.HashContainer;
import hierophant.core.hash.HashFunction;

import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConsistentHashContainer<E> implements HashContainer<E> {
    
    private static final int defaultReplicas = 64;
    
    protected final HashFunction func;
    
    protected final int replicas;
    
    protected final SortedMap<Long, E> circle = Collections.synchronizedSortedMap(new TreeMap<Long, E>());
    
    protected final CopyOnWriteArrayList<E> values = new CopyOnWriteArrayList<E>();
    
    public ConsistentHashContainer(final HashFunction func){
        this(func, defaultReplicas);
    }
    
    public ConsistentHashContainer(final HashFunction func, final int replicas){
        this.func = func;
        this.replicas = replicas;
    }
    
    public void add(E e){
        for(int i = 0; i < replicas; ++i){
            Long hash = Long.valueOf(func.hash(e.toString() + i));
            circle.put(hash, e);
        }
        values.add(e);
    }
    
    public void remove(E e){
        for(int i = 0; i < replicas; ++i){
            Long hash = Long.valueOf(func.hash(e.toString() + i));
            circle.remove(hash);
        }
    }
    
    public E get(String key){
        if(circle.isEmpty()){
            return null;
        }
        
        Long hash = Long.valueOf(func.hash(key));
        if(circle.containsKey(hash)){
            return circle.get(key);
        }
        
        SortedMap<Long, E> tail = circle.tailMap(hash);
        if(tail.isEmpty()){
            return circle.get(circle.firstKey());
        }
        return circle.get(tail.firstKey());
    }
    
    public List<E> values(){
        return values;
    }

}
