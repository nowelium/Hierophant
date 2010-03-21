package hierophant.memcache.worker.impl;

import hierophant.core.cache.Cache;
import hierophant.core.cache.Cache.Entry;
import hierophant.core.io.impl.ByteBufferReader;
import hierophant.memcache.MemcacheCache;
import hierophant.memcache.worker.NioAction;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioAcceptAction implements NioAction {
    
    protected final long maxMemory;
    
    protected final Cache<String, Entry<String, String>> coreCache;
    
    protected final MemcacheCache memcache;
    
    public NioAcceptAction(final long maxMemory, final Cache<String, Entry<String, String>> cache){
        this.maxMemory = maxMemory;
        this.coreCache = cache;
        this.memcache = new MemcacheCache(maxMemory, cache);
    }

    public void execute(final SelectionKey selectionKey) {
        SocketChannel channel = null;
        try {
            final Selector selector = selectionKey.selector();
            channel = ((ServerSocketChannel) selectionKey.channel()).accept();
            channel.configureBlocking(false);
            
            NioReadAction read = new NioReadAction(memcache, new ByteBufferReader(channel));
            channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, read);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(channel.finishConnect()){
                    //channel.close();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
