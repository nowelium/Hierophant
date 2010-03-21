package hierophant.memcache.command;

import hierophant.memcache.Parameter;
import hierophant.memcache.Return;
import hierophant.memcache.jjt.MemcacheParserVisitor;
import hierophant.memcache.jjt.Node;

public abstract class Command implements CommandCall {
    
    protected Node node;
    
    public void setNode(Node node){
        this.node = node;
    }
    public Node getNode(){
        return node;
    }
    
    public Return accept(MemcacheParserVisitor visitor, Parameter parameter){
        return node.jjtAccept(visitor, parameter);
    }
    
    public Return accept(CommandVisitor visitor, Parameter parameter){
        return visitor.visit(this, parameter);
    }

}
