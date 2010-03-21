package hierophant.core.cache.impl;

import hierophant.core.cache.Cache.Entry;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class LRUEntry<K, V> implements Entry<K, V>, Delayed {
    
    public static final long MAX_COUNTER_SIZE = 10;
    
    public static final long MIN_COUNTER_SIZE = 1;
    
    public static final long DEFAULT_COUNTER_SIZE = 5;
    
    protected final K key;
    
    protected final AtomicLong flag;
    
    protected final AtomicReference<V> value;
    
    protected final AtomicLong initialExpiredAt;
    
    protected final AtomicLong expiredAt;
    
    protected final AtomicLong counter = new AtomicLong(DEFAULT_COUNTER_SIZE);
    
    public LRUEntry(final K key, final V value, final long flag, final long expiredAt){
        this.key = key;
        this.value = new AtomicReference<V>(value);
        this.flag = new AtomicLong(flag);
        this.initialExpiredAt = new AtomicLong(expiredAt);
        this.expiredAt = new AtomicLong(relative(expiredAt));
    }
    
    protected static long relative(long expiredAt){
        // 0 指定は常に long 最大値
        if(0 == expiredAt){
            return Long.MAX_VALUE;
        }
        if(expiredAt < 0){
            return 0;
        }
        // 現在時間 + 未来時間
        return System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expiredAt);
    }
    
    public K getKey(){
        return key;
    }
    
    public long getFlag() {
        return flag.get();
    }
    public void setFlag(long newValue){
        flag.set(newValue);
    }
    
    public V getValue() {
        return value.get();
    }
    public void setValue(V newValue){
        value.set(newValue);
    }
    
    public long getExpiredAt(){
        return initialExpiredAt.get();
    }
    public void setExpiredAt(long newValue){
        initialExpiredAt.set(newValue);
        // 現在時間より次の期限を設定
        expiredAt.set(relative(newValue));
    }

    public long count(){
        return counter.get();
    }
    public long increment(){
        if(counter.get() < MAX_COUNTER_SIZE){
            return counter.incrementAndGet();
        }
        return MAX_COUNTER_SIZE;
    }
    public long decrement(){
        if(MIN_COUNTER_SIZE < counter.get()){
            return counter.decrementAndGet();
        }
        return MIN_COUNTER_SIZE;
    }

    private long elapsed(){
        return expiredAt.get() - System.currentTimeMillis();
    }
    
    public boolean isExpired(){
        return elapsed() < 1;
    }

    public long getDelay(TimeUnit unit) {
        // expiredの時間から経過時間を引き、残り時間を算出
        return unit.convert(elapsed(), TimeUnit.MILLISECONDS);
    }
    
    @SuppressWarnings("unchecked")
    public int compareTo(Delayed o) {
        LRUEntry<K, V> target = (LRUEntry<K, V>) o;
        final long e = expiredAt.get();
        final long t = target.expiredAt.get();
        if(e < t){
            return -1;
        }
        if(e > t){
            return 1;
        }
        return 0;
    }
    
    @Override
    public String toString(){
        StringBuilder buf = new StringBuilder();
        buf.append("entry {");
        buf.append("key:").append(key).append(",");
        buf.append("flag:").append(flag).append(",");
        buf.append("expiredAt(rel):").append(initialExpiredAt).append(",");
        buf.append("count:").append(counter);
        return buf.toString();
    }

}
