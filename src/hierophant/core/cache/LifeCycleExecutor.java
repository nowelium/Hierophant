package hierophant.core.cache;

import hierophant.core.cache.Cache.Entry;

import java.util.concurrent.BlockingQueue;

public interface LifeCycleExecutor<K> {
    
    public void register(LifeCycle<K> cache, BlockingQueue<? extends Entry<K, ?>> queue);
    
    public void acquire();
    
    public void release();
}
