package hierophant.memcache.command;

import hierophant.memcache.Parameter;
import hierophant.memcache.Return;

public class ReplaceCommand extends StorageCommand {
    @Override
    public Return accept(CommandVisitor visitor, Parameter parameter){
        return visitor.visit(this, parameter);
    }
}
