package hierophant.core.cache;

public interface LifeCycle<K> {
    
    public void register(LifeCycleExecutor<K> executor);
    
    public boolean purgeCache(K key);

}
