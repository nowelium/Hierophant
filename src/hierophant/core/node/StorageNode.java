package hierophant.core.node;

public abstract class StorageNode<K, V> implements Node {
    
    public abstract boolean set(K key, V value);
    
    public abstract V get(K key);
    
    public abstract void remove(K key, long expiredAt);
    
    public abstract void flush(long expiredAt);
    
    @Override
    public final String toString(){
        return getUnique();
    }

}
