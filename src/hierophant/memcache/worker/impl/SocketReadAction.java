package hierophant.memcache.worker.impl;

import hierophant.core.cache.Cache;
import hierophant.core.cache.Cache.Entry;
import hierophant.core.io.Reader;
import hierophant.core.io.impl.StreamReader;
import hierophant.memcache.MemcacheCache;
import hierophant.memcache.MemcacheCommandHandler;
import hierophant.memcache.Return;
import hierophant.memcache.worker.SocketAction;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SocketReadAction implements SocketAction {
    
    private static final Log log = LogFactory.getLog(SocketReadAction.class);
    
    protected final Socket socket;
    
    protected final MemcacheCache memcache;
    
    public SocketReadAction(final Socket socket, final long maxMemory, final Cache<String, Entry<String, String>> cache){
        this.socket = socket;
        this.memcache = new MemcacheCache(maxMemory, cache);
    }
    
    public void run(){
        if(log.isInfoEnabled()){
            log.info("reading line [" + socket + "]");
        }
        
        try {
            final InputStream in = socket.getInputStream();
            final OutputStream out = socket.getOutputStream();
            
            final Reader reader = new StreamReader(new InputStreamReader(in));
            final DataOutputStream writer = new DataOutputStream(out);
            final MemcacheCommandHandler handler = new MemcacheCommandHandler(memcache, reader);
            while(socket.isConnected()){
                String line = reader.readLine();
                if(null == line){
                    return;
                }
                Return ret = handler.execute(line);
                writer.writeBytes(ret.renderMessage());
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            if(log.isInfoEnabled()){
                log.info("connection closed.[" + socket + "]");
            }

            try {
                socket.close();
            } catch(IOException e){
                e.printStackTrace();
                // nop
            }
        }
    }

}
