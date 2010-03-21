package hierophant.memcache.worker.impl;

import hierophant.core.cache.Cache;
import hierophant.core.cache.Cache.Entry;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NioSocketDelegateAcceptAction extends NioAcceptAction {
    
    protected final ExecutorService pool = Executors.newFixedThreadPool(32);

    public NioSocketDelegateAcceptAction(final long maxMemory, final Cache<String, Entry<String, String>> cache) {
        super(maxMemory, cache);
    }

    public void execute(final SelectionKey selectionKey) {
        SocketChannel channel = null;
        try {
            channel = ((ServerSocketChannel) selectionKey.channel()).accept();
            
            pool.execute(new SocketReadAction(channel.socket(), maxMemory, coreCache));
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                if(channel.finishConnect()){
                    //channel.close();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
            
            pool.shutdown();
            try {
                if(!pool.awaitTermination(10, TimeUnit.SECONDS)){
                    pool.shutdownNow();
                }
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
