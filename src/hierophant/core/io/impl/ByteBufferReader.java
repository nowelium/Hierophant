package hierophant.core.io.impl;

import hierophant.core.io.Reader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ByteBufferReader implements Reader {
    
    protected final SocketChannel channel;
    
    protected final ByteBuffer buffer;
    
    public ByteBufferReader(final SocketChannel channel){
        this.channel = channel;
        this.buffer = ByteBuffer.allocateDirect(512);
    }
    
    public boolean readable() throws IOException {
        return buffer.hasRemaining();
    }
    
    public String readLine() throws IOException {
        final StringBuilder sb = new StringBuilder();
        readInto(sb);
        if(sb.length() < 1){
            return null;
        }
        return sb.toString();
    }
    
    private void readInto(final StringBuilder sb) throws IOException {
        while(true){
            int read = channel.read(buffer);
            if(read == 0){
                break;
            }
            if(read == -1){
                return;
            }
        }
        buffer.flip();
        while(more(sb));
        buffer.compact();
    }
    
    private boolean more(final StringBuilder sb) throws IOException {
        if(buffer.hasRemaining()){
            int read = buffer.get();
            if(read == -1){
                System.err.println("----------------");
                return false;
            }
            if(read == 0){
                System.err.println("@@@@@@@@@@@");
                buffer.clear();
                return true;
            }
            
            char ch = (char) read;
            switch(ch){
            case '\n':
                return false;
            case '\r':
                return true;
            default:
                sb.append(ch);
                return true;
            }
        }
        buffer.clear();
        return true;
    }

}
