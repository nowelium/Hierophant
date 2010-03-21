package hierophant.core.cache.impl;

import hierophant.core.cache.LifeCycleExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

public class LRULifeCycleTest {
    
    @Test
    public void test_1(){
        LifeCycleExecutor<String> exec = new DefaultLifeCycleExecutor<String>(Executors.newCachedThreadPool());
        LRUCache<String, String> cache = new LRUCache<String, String>();
        cache.register(exec);
        
        cache.set("hoge", new LRUEntry<String, String>("hoge", "hogeV", 0, 1));
        cache.set("foo", new LRUEntry<String, String>("foo", "fooV", 0, 0));
        
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch(InterruptedException e){
            Assert.fail(e.getMessage());
        }
    }

}
