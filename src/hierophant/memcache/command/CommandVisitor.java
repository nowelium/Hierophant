package hierophant.memcache.command;

import hierophant.memcache.Parameter;
import hierophant.memcache.Return;

public interface CommandVisitor {
    public Return visit(Command command, Parameter parameter);
    public Return visit(AddCommand command, Parameter parameter);
    public Return visit(AppendCommand command, Parameter parameter);
    public Return visit(CasCommand command, Parameter parameter);
    public Return visit(DeleteCommand command, Parameter parameter);
    public Return visit(PrependCommand command, Parameter parameter);
    public Return visit(ReplaceCommand command, Parameter parameter);
    public Return visit(RetrievalCommand command, Parameter parameter);
    public Return visit(SetCommand command, Parameter parameter);
    public Return visit(VersionCommand command, Parameter parameter);
}
