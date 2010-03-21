package hierophant.memcache;

import hierophant.core.cache.Cache.Entry;
import hierophant.core.io.Reader;
import hierophant.memcache.Return.ResponseType;
import hierophant.memcache.command.AddCommand;
import hierophant.memcache.command.AppendCommand;
import hierophant.memcache.command.CasCommand;
import hierophant.memcache.command.Command;
import hierophant.memcache.command.CommandVisitor;
import hierophant.memcache.command.DeleteCommand;
import hierophant.memcache.command.PrependCommand;
import hierophant.memcache.command.ReplaceCommand;
import hierophant.memcache.command.RetrievalCommand;
import hierophant.memcache.command.SetCommand;
import hierophant.memcache.command.VersionCommand;
import hierophant.memcache.jjt.MemcacheParser;
import hierophant.memcache.jjt.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MemcacheCommandHandler implements CommandVisitor {
    private static final Log log = LogFactory.getLog(MemcacheCommandHandler.class);
    
    protected static final InputStream nullInputStream = new InputStream(){
        @Override
        public int read() throws IOException {
            return 0;
        }
    };
    
    protected final MemcacheParser parser = new MemcacheParser(nullInputStream);
    
    protected final MemcacheCache cache;
    
    protected final Reader reader;
    
    public MemcacheCommandHandler(final MemcacheCache cache, final Reader reader){
        this.cache = cache;
        this.reader = reader;
    }
    public Return execute(String line){
        try {
            if(null == line){
                return new Return(ResponseType.ERROR);
            }
            
            parser.ReInit(new StringReader(line));
            Command command = parser.Command();
            return command.accept(this, null);
        } catch(ParseException e){
            e.printStackTrace();
            return new Return(ResponseType.ERROR);
        } catch(Exception e){
            e.printStackTrace();
            return new Return(ResponseType.SERVER_ERROR, e.getMessage());
        }
    }
    public Return visit(Command command, Parameter parameter) {
        return null;
    }

    public Return visit(AddCommand command, Parameter parameter) {
        return null;
    }

    public Return visit(AppendCommand command, Parameter parameter) {
        return null;
    }

    public Return visit(CasCommand command, Parameter parameter) {
        return null;
    }

    public Return visit(DeleteCommand command, Parameter parameter) {
        return null;
    }

    public Return visit(PrependCommand command, Parameter parameter) {
        return null;
    }

    public Return visit(ReplaceCommand command, Parameter parameter) {
        return null;
    }

    public Return visit(RetrievalCommand command, Parameter parameter) {
        final List<String> keys = command.getKeys();
        final List<Return> returns = new ArrayList<Return>();
        for(final String key: keys){
            Entry<String, String> entry = cache.get(key);
            if(null == entry){
                continue;
            }
            long flag = entry.getFlag();
            String value = entry.getValue();
            long length = value.length();
            returns.add(new Return(ResponseType.SEND_VALUE,
                key, flag, length, value
            ));
        }
        return new Return(returns.toArray(new Return[returns.size()]));
    }

    public Return visit(SetCommand command, Parameter parameter) {
        try {
            String key = command.getKey();
            long flag = command.getFlags();
            long expired = command.getExpTime();
            
            String value = reader.readLine();
            long length = value.length();
            
            if(length != command.getLength()){
                if(log.isDebugEnabled()){
                    log.debug("specified length was not each");
                }
                return new Return(ResponseType.ERROR);
            }
            if(cache.set(key, value, flag, expired)){
                return new Return(ResponseType.STORED);
            }
            return new Return(ResponseType.NOT_STORED);
        } catch(IOException e){
            if(log.isErrorEnabled()){
                log.error("error", e);
            }
            return new Return(ResponseType.ERROR);
        }
    }

    public Return visit(VersionCommand command, Parameter parameter) {
        return null;
    }

}
