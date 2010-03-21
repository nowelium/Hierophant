package hierophant.core.cache.impl;

import hierophant.core.cache.LifeCycle;
import hierophant.core.cache.LifeCycleExecutor;
import hierophant.core.cache.Cache.Entry;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultLifeCycleExecutor<K> implements LifeCycleExecutor<K> {
    
    private static final Log log = LogFactory.getLog(DefaultLifeCycleExecutor.class);
    
    private final ExecutorService executor;
    
    private final AtomicInteger counter = new AtomicInteger(0);
    
    public DefaultLifeCycleExecutor(final ExecutorService executor){
        this.executor = executor;
    }
    
    public void register(LifeCycle<K> target, BlockingQueue<? extends Entry<K, ?>> queue) {
        executor.execute(new Check<K>(target, queue));
    }
    
    public void acquire(){
        counter.incrementAndGet();
    }
    
    public void release(){
        counter.decrementAndGet();
    }
    
    protected static class Check<K> implements Runnable {
        private final LifeCycle<K> target;
        private final BlockingQueue<? extends Entry<K, ?>> queue;
        protected Check(final LifeCycle<K> target, final BlockingQueue<? extends Entry<K, ?>> queue){
            this.target = target;
            this.queue = queue;
        }
        public void run(){
            try {
                if(log.isInfoEnabled()){
                    log.info("gurbage collector waiting...");
                }
                while(true){
                    Entry<K, ?> entry = queue.take();
                    if(log.isDebugEnabled()){
                        log.debug("dequeue => " + entry);
                    }
                    target.purgeCache(entry.getKey());
                }
            } catch(InterruptedException e){
                e.printStackTrace();
            } finally {
                if(log.isInfoEnabled()){
                    log.info("gurbage collector will shutdown...");
                }
            }
        }
    }
}
