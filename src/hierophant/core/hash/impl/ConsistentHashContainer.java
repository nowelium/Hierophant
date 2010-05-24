package hierophant.core.hash.impl;

import hierophant.core.hash.Circle;
import hierophant.core.hash.HashContainer;
import hierophant.core.hash.HashFunction;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConsistentHashContainer<E> implements HashContainer<E> {
    
    protected static final int defaultReplicas = 128;
    
    protected final HashFunction func;
    
    protected final int replicas;
    
    protected final Circle<E> circle = new SortedCircle<E>();
    
    protected final CopyOnWriteArrayList<E> values = new CopyOnWriteArrayList<E>();
    
    public ConsistentHashContainer(final HashFunction func){
        this(func, defaultReplicas);
    }
    
    public ConsistentHashContainer(final HashFunction func, final int replicas){
        super();
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
        values.remove(e);
    }
    
    public E get(String key){
        if(circle.isEmpty()){
            return null;
        }
        
        Long hash = Long.valueOf(func.hash(key));
        if(circle.containsKey(hash)){
            return get(hash);
        }
        
        Circle<E> tail = circle.tail(hash);
        if(tail.isEmpty()){
            return get(circle.firstKey());
        }
        return get(tail.firstKey());
    }
    
    protected E get(Long key){
        return circle.get(key);
    }
    
    public List<E> values(){
        return values;
    }
    
}
