/* Generated By:JJTree: Do not edit this line. NodeNoreply.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=Node,NODE_EXTENDS=hierophant.memcache.command.Command,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package hierophant.memcache.jjt;

public
class NodeNoreply extends SimpleNode {
  public NodeNoreply(int id) {
    super(id);
  }

  public NodeNoreply(MemcacheParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new NodeNoreply(id);
  }

  public static Node jjtCreate(MemcacheParser p, int id) {
    return new NodeNoreply(p, id);
  }

  /** Accept the visitor. **/
  public hierophant.memcache.Return jjtAccept(MemcacheParserVisitor visitor, hierophant.memcache.Parameter data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=c2de95ff8ebb569c4c473cad7e1572da (do not edit this line) */
