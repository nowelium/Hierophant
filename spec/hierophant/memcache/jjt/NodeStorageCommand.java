/* Generated By:JJTree: Do not edit this line. NodeStorageCommand.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=Node,NODE_EXTENDS=hierophant.memcache.command.Command,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package hierophant.memcache.jjt;

public
class NodeStorageCommand extends SimpleNode {
  public NodeStorageCommand(int id) {
    super(id);
  }

  public NodeStorageCommand(MemcacheParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new NodeStorageCommand(id);
  }

  public static Node jjtCreate(MemcacheParser p, int id) {
    return new NodeStorageCommand(p, id);
  }

  /** Accept the visitor. **/
  public hierophant.memcache.Return jjtAccept(MemcacheParserVisitor visitor, hierophant.memcache.Parameter data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=72e5094148546fc61b993f2a1bcc3b46 (do not edit this line) */
