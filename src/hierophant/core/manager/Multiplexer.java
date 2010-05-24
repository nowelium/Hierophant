package hierophant.core.manager;

import hierophant.core.Port;
import hierophant.core.cache.Cache;
import hierophant.core.cache.LifeCycleExecutor;
import hierophant.core.cache.Cache.Entry;
import hierophant.core.cache.impl.DefaultLifeCycleExecutor;
import hierophant.core.cache.impl.DistributedNodeCache;
import hierophant.core.hash.HashFunction;
import hierophant.core.hash.impl.ConsistentHashContainer;
import hierophant.core.hash.impl.Hash;
import hierophant.core.node.StorageNode;
import hierophant.core.node.impl.LoopBackStorageNode;
import hierophant.protobuf.storage.StorageReceiver;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.googlecode.protobuf.socketrpc.SocketRpcServer;

public class Multiplexer {
    
    protected final HashFunction function = Hash.MD5;
    
    protected final ConsistentHashContainer<StorageNode<String, Entry<String, String>>> container;
    
    protected final DistributedNodeCache<String, Entry<String, String>> cache;
    
    protected final int maxConnection;
    
    protected final LifeCycleExecutor<String> lifeCycle;
    
    protected final ExecutorService storageExecutor;
    
    protected final ScheduledExecutorService indexExecutor;
    
    protected final ExecutorService main = Executors.newCachedThreadPool();
    
    public Multiplexer(final int maxConnection){
        this.maxConnection = maxConnection;
        this.container = new ConsistentHashContainer<StorageNode<String, Entry<String, String>>>(function);
        this.cache = new DistributedNodeCache<String, Entry<String, String>>(container);
        this.lifeCycle = new DefaultLifeCycleExecutor<String>(main);
        this.storageExecutor = Executors.newFixedThreadPool(maxConnection);
        this.indexExecutor = Executors.newScheduledThreadPool(2);
    }
    
    public void initialize(){
        cache.addNode(new LoopBackStorageNode(lifeCycle));
        
        main.execute(new StorageServiceCommunicator());
        main.execute(new IndexServiceCommunicator());
    }
    
    public void destroy(){
        main.shutdown();
        
        try {
            if(!main.awaitTermination(30, TimeUnit.SECONDS)){
                main.shutdownNow();
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    
    public Cache<String, Entry<String, String>> getCache(){
        return cache;
    }
    
    protected class StorageServiceCommunicator implements Runnable {
        public void run(){
            StorageReceiver receiver = new StorageReceiver(cache);
            SocketRpcServer server = new SocketRpcServer(Port.Storage.getPort(), storageExecutor);
            server.registerBlockingService(receiver.createBlockingService());
            try {
                server.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    protected class IndexServiceCommunicator implements Runnable {
        public void run(){
            //indexExecutor.execute(new Service);
        }
    }

}
