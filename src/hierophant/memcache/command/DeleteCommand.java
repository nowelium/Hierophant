package hierophant.memcache.command;

import hierophant.memcache.Parameter;
import hierophant.memcache.Return;

public class DeleteCommand extends Command {
    protected String key;
    protected Long expTime;
    protected Boolean noreply;
    public void setKey(String key){
        this.key = key;
    }
    public void setExpTime(Long expTime){
        this.expTime = expTime;
    }
    public void setNoreply(Boolean noreply){
        this.noreply = noreply;
    }
    public String getKey(){
        return key;
    }
    public Long getExpTime() {
        return expTime;
    }
    public Boolean getNoreply() {
        return noreply;
    }
    
    @Override
    public Return accept(CommandVisitor visitor, Parameter parameter){
        return visitor.visit(this, parameter);
    }
}
