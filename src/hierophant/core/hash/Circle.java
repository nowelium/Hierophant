package hierophant.core.hash;

public interface Circle<E> {
    public void put(Long key, E e);
    
    public E get(Long key);
    
    public void remove(Long key);
    
    public Circle<E> tail(Long key);
    
    public Long firstKey();
    
    public boolean isEmpty();
    
    public boolean containsKey(Long key);
}
