package hierophant.core.cache.impl;

import hierophant.core.cache.Cache;
import hierophant.core.hash.impl.ConsistentHashContainer;
import hierophant.core.node.StorageNode;

public class DistributedNodeCache<K, V> implements Cache<K, V> {
    
    protected final ConsistentHashContainer<StorageNode<K, V>> container;
    
    public DistributedNodeCache(final ConsistentHashContainer<StorageNode<K, V>> container){
        this.container = container;
    }
    
    public void addNode(StorageNode<K, V> node){
        container.add(node);
    }
    
    public void removeNode(StorageNode<K, V> node){
        container.remove(node);
    }

    public boolean set(K key, V entry) {
        return container.get(key.toString()).set(key, entry);
    }
    
    public V get(K key) {
        return container.get(key.toString()).get(key);
    }

    public void remove(K key) {
        remove(key, 0);
    }

    public void remove(K key, long expiredAt) {
        container.get(key.toString()).remove(key, expiredAt);
    }

    public void flush() {
        flush(0);
    }

    public void flush(long expiredAt) {
        for(StorageNode<K, V> node: container.values()){
            node.flush(expiredAt);
        }
    }

}
