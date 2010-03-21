package hierophant.memcache.command;

import hierophant.memcache.Parameter;
import hierophant.memcache.Return;

public interface CommandCall {
    public Return accept(CommandVisitor visitor, Parameter parameter);
}
