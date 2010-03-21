package hierophant.core.node.impl;

import hierophant.core.cache.LifeCycleExecutor;
import hierophant.core.cache.Cache.Entry;
import hierophant.core.cache.impl.LRUCache;
import hierophant.core.cache.impl.LRUEntry;
import hierophant.core.node.StorageNode;

public class LoopBackStorageNode extends StorageNode<String, Entry<String, String>> {
    
    private static final String UNIQUE = LoopBackStorageNode.class.getName();
    
    protected final LRUCache<String, String> cache;
    
    protected final LifeCycleExecutor<String> executor;
    
    public LoopBackStorageNode(final LifeCycleExecutor<String> executor){
        this.cache = new LRUCache<String, String>();
        this.executor = executor;
        cache.register(executor);
    }
    
    public String getUnique(){
        return UNIQUE;
    }
    
    @Override
    public void flush(long expiredAt) {
        cache.flush(expiredAt);
    }

    @Override
    public Entry<String, String> get(String key) {
        return cache.get(key);
    }

    @Override
    public void remove(String key, long expiredAt) {
        cache.remove(key, expiredAt);
    }

    @Override
    public boolean set(String key, Entry<String, String> value) {
        return cache.set(key, new LRUEntry<String, String>(
            value.getKey(),
            value.getValue(),
            value.getFlag(),
            value.getExpiredAt()
        ));
    }

}
