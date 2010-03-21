package hierophant.memcache;

import hierophant.core.cache.Cache;
import hierophant.core.cache.Cache.Entry;
import hierophant.core.cache.impl.LRUEntry;
import hierophant.core.exception.MemoryAllocationRuntimeException;

public class MemcacheCache {
    
    protected final Cache<String, Entry<String, String>> cache;

    protected final long maxMemory;
    
    protected final Runtime runtime;
    
    protected long currentFreeMemory;
    
    public MemcacheCache(final long maxMemory, final Cache<String, Entry<String, String>> cache){
        this.cache = cache;
        this.maxMemory = maxMemory;
        this.runtime = Runtime.getRuntime();
        if(runtime.freeMemory() < maxMemory){
            throw new MemoryAllocationRuntimeException();
        }
        this.currentFreeMemory = runtime.freeMemory();
    }

    public boolean set(String key, String value, long flag, long expiredAt) {
        if(!isAllocatableFreeSpace()){
            throw new MemoryAllocationRuntimeException();
        }
        return cache.set(key, new LRUEntry<String, String>(key, value, flag, expiredAt));
    }

    public Entry<String, String> get(String key) {
        return cache.get(key);
    }
    
    public void flush() {
        cache.flush();
    }

    public void flush(long expiredAt) {
        cache.flush(expiredAt);
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public void remove(String key, long expiredAt) {
        cache.remove(key, expiredAt);
    }

    protected boolean isAllocatableFreeSpace(){
        final long diff = currentFreeMemory - runtime.freeMemory();
        return diff < maxMemory;
    }

}
