/* Generated By:JJTree: Do not edit this line. NodeCommand.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=Node,NODE_EXTENDS=hierophant.memcache.command.Command,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package hierophant.memcache.jjt;

public
class NodeCommand extends SimpleNode {
  public NodeCommand(int id) {
    super(id);
  }

  public NodeCommand(MemcacheParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new NodeCommand(id);
  }

  public static Node jjtCreate(MemcacheParser p, int id) {
    return new NodeCommand(p, id);
  }

  /** Accept the visitor. **/
  public hierophant.memcache.Return jjtAccept(MemcacheParserVisitor visitor, hierophant.memcache.Parameter data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7e379c9dbd7c4346c9ccb703b0dfee41 (do not edit this line) */
