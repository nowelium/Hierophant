package hierophant.memcache.worker.impl;

import hierophant.core.io.Reader;
import hierophant.memcache.MemcacheCache;
import hierophant.memcache.MemcacheCommandHandler;
import hierophant.memcache.Return;
import hierophant.memcache.worker.NioAction;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NioReadAction implements NioAction {
    
    private static final Log log = LogFactory.getLog(NioReadAction.class);
    
    private static final Charset ASCII = Charset.forName("US-ASCII");
    
    protected final MemcacheCache cache;
    
    protected final Reader reader;
    
    protected final MemcacheCommandHandler readHandler;
    
    public NioReadAction(final MemcacheCache cache, final Reader reader){
        this.cache = cache;
        this.reader = reader;
        this.readHandler = new MemcacheCommandHandler(cache, reader);
    }

    public void execute(SelectionKey selectionKey) {
        if(!selectionKey.isReadable()){
            return;
        }
        
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        try {
            if(!reader.readable()){
                channel.close();
                selectionKey.cancel();
                return;
            }
            final String line = reader.readLine();
            if(line == null){
                channel.close();
                selectionKey.cancel();
                return;
            }
            Return ret = readHandler.execute(line);
            
            if(log.isDebugEnabled()){
                log.debug("send value " + ret.renderMessage());
            }
            
            final CharBuffer buf = CharBuffer.wrap(ret.renderMessage());
            ByteBuffer bytes = ASCII.encode(buf);
            int capacity = bytes.capacity();
            int size = channel.write(bytes);
            if(size < capacity){
                int sum = size;
                while(sum < capacity){
                    if(size == 0){
                        return;
                    }
                    bytes.position(sum);
                    size = channel.write(bytes);
                    sum += size;
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

}
