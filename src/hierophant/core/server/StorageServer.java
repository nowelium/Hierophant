package hierophant.core.server;

import hierophant.core.Port;
import hierophant.core.manager.Multiplexer;
import hierophant.core.util.ByteSizeUnit;
import hierophant.memcache.worker.NioAction;
import hierophant.memcache.worker.impl.NioAcceptAction;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StorageServer extends Thread {
    
    protected static final Log log = LogFactory.getLog(StorageServer.class);
    
    protected final int port;
    
    protected final int maxConnection;
    
    protected final long maxMemory;
    
    protected final Multiplexer mux;
    
    public StorageServer(final int port, final int maxConnection){
        this(port, maxConnection, ByteSizeUnit.Mega.toLong(64));
    }
    public StorageServer(final int port, final int maxConnection, final long maxMemory){
        this.port = port;
        this.maxConnection = maxConnection;
        this.maxMemory = maxMemory;
        this.mux = new Multiplexer(maxConnection);
    }
    
    public static void main(String...args){
        StorageServer s = new StorageServer(Port.MemcacheServer.getPort(), 32);
        s.start();
        try {
            s.join();
        } catch(InterruptedException e){
            e.printStackTrace(System.err);
        }
    }
    
    public void run(){
        if(log.isInfoEnabled()){
            log.info("StorageServer run on port: " + port);
        }
        mux.initialize();
        
        ServerSocketChannel channel = null;
        try {
            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            
            final ServerSocket serverSocket = channel.socket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(port));
            
            final Selector selector = Selector.open();
            try {
                channel.register(selector, SelectionKey.OP_ACCEPT, new NioAcceptAction(maxMemory, mux.getCache()));
                
                while(0 < selector.select()){
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    while(keys.hasNext()){
                        final SelectionKey key = keys.next();
                        keys.remove();
                        
                        if(!key.isValid()){
                            continue;
                        }
                        
                        NioAction action = (NioAction) key.attachment();
                        action.execute(key);
                    }
                }
            } finally {
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while(keys.hasNext()){
                    final SelectionKey key = keys.next();
                    key.channel().close();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != channel){
                try {
                    channel.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
