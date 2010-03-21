package hierophant.memcache.command;

import hierophant.memcache.Parameter;
import hierophant.memcache.Return;

import java.util.ArrayList;
import java.util.List;

public class RetrievalCommand extends Command {
    private final List<String> keys = new ArrayList<String>();
    
    public void addKey(String key){
        keys.add(key);
    }
    
    public List<String> getKeys(){
        return keys;
    }
    
    @Override
    public Return accept(CommandVisitor visitor, Parameter parameter){
        return visitor.visit(this, parameter);
    }
}
