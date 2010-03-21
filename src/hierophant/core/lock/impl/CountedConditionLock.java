package hierophant.core.lock.impl;

import hierophant.core.lock.ConditionLock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class CountedConditionLock implements ConditionLock {
    
    protected static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 8011349365914441514L;
        public int count(){
            return getState();
        }
        public boolean update(int arg){
            int c = getState();
            return compareAndSetState(c, arg);
        }
        public int tryAcquireShared(int arg){
            return getState();
        }
        public boolean tryReleaseShared(int releases) {
            return getState() == 0;
        }
    }
    
    private final AtomicInteger counter = new AtomicInteger(1);
    
    protected final Sync sync = new Sync();
    
    public void countUp(){
        sync.update(counter.incrementAndGet());
    }
    
    public void countDown(){
        sync.update(counter.decrementAndGet());
    }
    
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public void release() {
        sync.releaseShared(1);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[Count = " + sync.count() + "]";
    }

}
