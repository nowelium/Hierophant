package hierophant.core.cache;

public interface Cache<K, E> {
    
    public boolean set(K key, E entry);
    
    public E get(K key);
    
    public void remove(K key);
    
    public void remove(K key, long expiredAt);
    
    public void flush();
    
    public void flush(long expiredAt);
    
    public interface Entry<K, V> {
        
        public K getKey();
        
        public long getFlag();

        public V getValue();
        
        public long getExpiredAt();
    }
    
}
