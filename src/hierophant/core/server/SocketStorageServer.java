package hierophant.core.server;

import hierophant.core.Port;
import hierophant.core.util.ByteSizeUnit;
import hierophant.memcache.worker.impl.SocketReadAction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ServerSocketFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SocketStorageServer extends StorageServer {
    
    private static final Log log = LogFactory.getLog(SocketStorageServer.class);
    
    protected ExecutorService pool;

    public SocketStorageServer(final int port, final int maxConnection) {
        this(port, maxConnection, ByteSizeUnit.Mega.toLong(64));
    }
    
    public SocketStorageServer(final int port, final int maxConnection, final long maxMemory){
        super(port, maxConnection, maxMemory);
        this.pool = Executors.newCachedThreadPool();
    }
    
    public static void main(String...args){
        SocketStorageServer s = new SocketStorageServer(Port.MemcacheServer.getPort(), 8);
        s.start();
        try {
            s.join();
        } catch(InterruptedException e){
            e.printStackTrace(System.err);
        }
    }
    
    public void run(){
        try {
            if(log.isInfoEnabled()){
                log.info("start server on port: " + port);
            }
            mux.initialize();
            
            ServerSocketFactory factory = ServerSocketFactory.getDefault();
            ServerSocket socket = factory.createServerSocket(port, 2048);
            socket.setReuseAddress(true);
            
            while(!socket.isClosed()){
                final Socket accept = socket.accept();
                accept.setKeepAlive(true);
                accept.setReuseAddress(true);
                accept.setTcpNoDelay(true);
                accept.setPerformancePreferences(0, 1, 2);
                
                if(log.isDebugEnabled()){
                    log.debug("new connection: [" + accept + "]");
                }
                pool.execute(new SocketReadAction(accept, maxMemory, mux.getCache()));
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
            try {
                if(!pool.awaitTermination(10, TimeUnit.SECONDS)){
                    pool.shutdownNow();
                }
            } catch(InterruptedException e){
                e.printStackTrace();
            }
            
            mux.destroy();
            
            if(log.isInfoEnabled()){
                log.info("server shutdown.");
            }
        }
    }
}
