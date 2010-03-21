package hierophant.core.cache.impl;

import hierophant.core.cache.Cache;
import hierophant.core.cache.LifeCycle;
import hierophant.core.cache.LifeCycleExecutor;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LRUCache<K, V> implements LifeCycle<K>, Cache<K, LRUEntry<K, V>> {
    
    private static final Log log = LogFactory.getLog(LRUCache.class);
    
    protected final DelayQueue<LRUEntry<K, V>> queue = new DelayQueue<LRUEntry<K, V>>();
    
    protected final ConcurrentMap<K, LRUEntry<K, V>> cache;
    
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    private final Lock readLock = lock.readLock();
    
    private final Lock writeLock = lock.writeLock();
    
    public LRUCache(){
        this(new ConcurrentHashMap<K, LRUEntry<K, V>>());
    }
    
    public LRUCache(final ConcurrentMap<K, LRUEntry<K, V>> cache){
        this.cache = cache;
    }
    
    public void register(LifeCycleExecutor<K> executor){
        executor.register(this, queue);
    }
    
    public boolean purgeCache(K key){
        writeLock.lock();
        try {
            if(!cache.containsKey(key)){
                return true;
            }
            LRUEntry<K, V> entry = cache.get(key);
            // decrement counter
            if(entry.decrement() < 1){
                if(log.isDebugEnabled()){
                    log.debug("[gc] cache key: " + key + " will removed");
                }
                return cache.remove(key, entry);
            }
            entry.setExpiredAt(retransmission(entry.count()));
            cache.replace(key, entry);

            if(log.isDebugEnabled()){
                log.debug("retransmission: " + entry);
            }
            return queue.offer(entry);
        } finally {
            writeLock.unlock();
        }
    }
    
    public boolean set(final K key, final LRUEntry<K, V> entry) {
        writeLock.lock();
        try {
            LRUEntry<K, V> previous = cache.putIfAbsent(key, entry);
            if(null == previous){
                // is new entry
                return queue.offer(entry);
            }
            // exists entry for update
            previous.setValue(entry.getValue());
            previous.setFlag(entry.getFlag());
            previous.setExpiredAt(entry.getExpiredAt());
            previous.increment();
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    public LRUEntry<K, V> get(K key) {
        readLock.lock();
        try {
            if(!cache.containsKey(key)){
                return null;
            }
            LRUEntry<K, V> entry = cache.get(key);
            // unvisible
            if(entry.isExpired()){
                return null;
            }
            entry.increment();
            return entry;
        } finally {
            readLock.unlock();
        }
    }

    public void remove(K key) {
        remove(key, 0);
    }
    
    public void remove(K key, long expiredAt){
        writeLock.lock();
        try {
            LRUEntry<K, V> entry = cache.get(key);
            if(null != entry){
                entry.setExpiredAt(expiredAt);
                entry.decrement();
            }
        } finally {
            writeLock.unlock();
        }
    }
    
    public void flush() {
        flush(0);
    }
    
    public void flush(long expiredAt){
        writeLock.lock();
        try {
            Iterator<Map.Entry<K, LRUEntry<K, V>>> entries = cache.entrySet().iterator();
            while(entries.hasNext()){
                final Map.Entry<K, LRUEntry<K, V>> entry = entries.next();
                final LRUEntry<K, V> value = entry.getValue();
                value.setExpiredAt(expiredAt);
                value.decrement();
            }
        } finally {
            writeLock.unlock();
        }
    }
    
    protected static long retransmission(final long count){
        if(count < 25){
            // binary exponential backoff
            long freq = count + Math.round(Math.pow(2, count));
            return Math.round(0.875 * freq) + Math.round(0.125 * count);
        }
        // 365.2422 * 86400
        return 31556926;
    }
    
}
