/* Generated By:JJTree: Do not edit this line. NodeKey.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=Node,NODE_EXTENDS=hierophant.memcache.command.Command,NODE_FACTORY=*,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package hierophant.memcache.jjt;

public
class NodeKey extends SimpleNode {
  public NodeKey(int id) {
    super(id);
  }

  public NodeKey(MemcacheParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
    return new NodeKey(id);
  }

  public static Node jjtCreate(MemcacheParser p, int id) {
    return new NodeKey(p, id);
  }

  /** Accept the visitor. **/
  public hierophant.memcache.Return jjtAccept(MemcacheParserVisitor visitor, hierophant.memcache.Parameter data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=363170440d465be5951a2c4f39ecc8e7 (do not edit this line) */
