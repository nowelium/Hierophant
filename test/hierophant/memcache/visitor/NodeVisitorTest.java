package hierophant.memcache.visitor;

import hierophant.memcache.Parameter;
import hierophant.memcache.Return;
import hierophant.memcache.command.AddCommand;
import hierophant.memcache.command.AppendCommand;
import hierophant.memcache.command.CasCommand;
import hierophant.memcache.command.Command;
import hierophant.memcache.command.CommandVisitor;
import hierophant.memcache.command.DeleteCommand;
import hierophant.memcache.command.PrependCommand;
import hierophant.memcache.command.ReplaceCommand;
import hierophant.memcache.command.RetrievalCommand;
import hierophant.memcache.command.SetCommand;
import hierophant.memcache.command.VersionCommand;
import hierophant.memcache.jjt.MemcacheParser;
import hierophant.memcache.jjt.ParseException;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

public class NodeVisitorTest {

    @Test
    public void set(){
        StringReader reader = new StringReader("set xyzkey 0 0 6\r\n");
        MemcacheParser parser = new MemcacheParser(reader);
        try {
            Parameter param = new Parameter();
            parser.Command().accept(new t_visitor(), param);
        } catch(ParseException e){
            Assert.fail(e.getMessage());
        }
    }
    
    private static class t_visitor implements CommandVisitor {
        public Return visit(AddCommand command, Parameter parameter) {
            Assert.fail("fail method");
            return null;
        }
        public Return visit(AppendCommand command, Parameter parameter) {
            Assert.fail("fail method");
            return null;
        }
        public Return visit(CasCommand command, Parameter parameter) {
            Assert.fail("fail method");
            return null;
        }
        public Return visit(DeleteCommand command, Parameter parameter) {
            Assert.fail("fail method");
            return null;
        }
        public Return visit(PrependCommand command, Parameter parameter) {
            Assert.fail("fail method");
            return null;
        }
        public Return visit(ReplaceCommand command, Parameter parameter) {
            Assert.fail("fail method");
            return null;
        }
        public Return visit(RetrievalCommand command, Parameter parameter) {
            Assert.fail("fail method");
            return null;
        }
        public Return visit(SetCommand command, Parameter parameter) {
            System.out.println("hello world");
            return null;
        }
        public Return visit(VersionCommand command, Parameter parameter) {
            Assert.fail("fail method");
            return null;
        }
        public Return visit(Command command, Parameter parameter) {
            Assert.fail("fail method");
            return null;
        }
    }
    
}
