package hierophant.core.lock;

public interface ConditionLock {
    
    public void await() throws InterruptedException;
    
    public void release();

}
