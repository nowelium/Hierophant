package hierophant.core.cache.impl;

import org.junit.Assert;
import org.junit.Test;

public class LRUCache2Test {
    @Test
    public void test_retransmission(){
        Assert.assertEquals(LRUCache.retransmission(10L), 906L);
        Assert.assertEquals(LRUCache.retransmission(20L), 917525L);
        Assert.assertEquals(LRUCache.retransmission(25L), 29360153L);
        Assert.assertEquals(LRUCache.retransmission(28L), 234881053L);
        Assert.assertEquals(LRUCache.retransmission(30L), 939524126L);
        Assert.assertEquals(LRUCache.retransmission(50L), 985162418487346L);
        Assert.assertEquals(LRUCache.retransmission(60L), 1008806316530991112L);
    }
    
}
