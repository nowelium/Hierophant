package hierophant.memcache.command;

import hierophant.memcache.Parameter;
import hierophant.memcache.Return;

public abstract class StorageCommand extends Command {
    protected String key;
    protected Long flags;
    protected Long expTime;
    protected Long length;
    protected Boolean noreply;
    public void setKey(String key){
        this.key = key;
    }
    public void setFlags(Long flags){
        this.flags = flags;
    }
    public void setExpTime(Long expTime){
        this.expTime = expTime;
    }
    public void setLength(Long length){
        this.length = length;
    }
    public void setNoreply(Boolean noreply){
        this.noreply = noreply;
    }
    public String getKey(){
        return key;
    }
    public Long getFlags() {
        return flags;
    }
    public Long getExpTime() {
        return expTime;
    }
    public Long getLength() {
        return length;
    }
    public Boolean getNoreply() {
        return noreply;
    }
    
    @Override
    public Return accept(CommandVisitor visitor, Parameter parameter){
        return visitor.visit(this, parameter);
    }
}
