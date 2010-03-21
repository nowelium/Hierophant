package hierophant.core.cache.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LRUCacheTest {
    
    protected static interface ConcurrentCheck {
        public void execute();
    }
    
    private LRUCache<String, String> cache;
    
    private BlockingQueue<ConcurrentCheck> afterCheck;
    
    private ExecutorService exec;
    
    @Before
    public void setupCache(){
        cache = new LRUCache<String, String>();
    }
    
    @Before
    public void setupConcurrent(){
        afterCheck = new LinkedBlockingQueue<ConcurrentCheck>();
        exec = Executors.newSingleThreadExecutor();
        exec.execute(new Runnable(){
            public void run(){
                try {
                    while(true){
                        afterCheck.take().execute();
                    }
                } catch(InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    @After
    public void concurrentCheck(){
        if(afterCheck.size() < 1){
            return;
        }
        exec.shutdown();
        try {
            if(exec.awaitTermination(10, TimeUnit.SECONDS)){
                exec.shutdownNow();
                Assert.fail("shutdown fail");
            }
        } catch(InterruptedException e){
            Assert.fail("shutdown fail");
        }
    }
    
    protected void set(String key, String value, long flag, long time){
        cache.set(key, new LRUEntry<String, String>(key, value, flag, time));
    }
    
    protected LRUEntry<String, String> get(String key){
        return cache.get(key);
    }
    
    @Test
    public void 指定時間で参照出来なくなる() throws InterruptedException {
        set("hoge", "hogeValue", 0, 2);
        Assert.assertEquals(get("hoge").getValue(), "hogeValue");

        TimeUnit.SECONDS.sleep(2);
        
        Assert.assertNull("参照できなくなってる", get("hoge"));
    }
    
    @Test
    public void 指定時間後に消えてる() throws InterruptedException {
        set("hoge", "hogeValue", 0, 2);

        TimeUnit.SECONDS.sleep(1);
        
        Assert.assertEquals(get("hoge").getValue(), "hogeValue");
        cache.remove("hoge", 5);

        TimeUnit.SECONDS.sleep(1);

        Assert.assertEquals("これは消えてるのが正解?", get("hoge").getValue(), "hogeValue");
    }
    
    @Test
    public void ゼロを指定すると消えない() throws InterruptedException {
        set("hoge", "hogeValue", 0, 0);
        set("foo", "fooValue", 0, 1);

        TimeUnit.SECONDS.sleep(1);

        Assert.assertNull("これは1秒後消えてる", get("foo"));
        Assert.assertEquals("これは消えない", get("hoge").getValue(), "hogeValue");
    }

    @Test
    public void キーがない(){
        set("hoge", "hogeValue", 0, 0);
        Assert.assertNull(cache.get("foo"));
    }
    
    @Test
    public void flushですぐにきえる() throws InterruptedException {
        set("hoge0", "value", 0, 100);
        cache.flush();
        Assert.assertNull(cache.get("hoge0"));
//        afterCheck.add(new ConcurrentCheck(){
//            public void execute(){
//                try {
//                    TimeUnit.MILLISECONDS.sleep(100);
//                } catch(InterruptedException e){
//                }
//                System.out.println(System.currentTimeMillis());
//                Assert.assertNotNull("本物のcacheも消えていること(0)", cache.cache.get("hoge0"));
//            }
//        });
        //TimeUnit.MILLISECONDS.sleep(500);
        
        Assert.assertNotNull("本物のcacheも消えていること(0)", cache.cache.get("hoge0"));
    }
    
    @Test
    public void flushで3秒後に消えること() throws InterruptedException {
        set("hoge1", "value", 0, 100);
        cache.flush(3);
        Assert.assertNotNull("すぐには消えていない", cache.get("hoge1"));
        Assert.assertNotNull("すぐには消えていない", cache.cache.get("hoge1"));

        TimeUnit.SECONDS.sleep(3);

        Assert.assertNull(cache.get("hoge1"));
//        afterCheck.add(new ConcurrentCheck(){
//            public void execute(){
//                try {
//                    TimeUnit.MILLISECONDS.sleep(100);
//                } catch(InterruptedException e){
//                    e.printStackTrace();
//                }
//                System.out.println(System.currentTimeMillis() + "::" + cache.cache.hashCode());
//                Assert.assertNotNull("本物のcacheも消えていること(1)", cache.cache.get("hoge1"));
//            }
//        });
        Assert.assertNotNull("本物のcacheも消えていること(1)", cache.cache.get("hoge1"));
    }
    
}
