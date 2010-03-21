package hierophant.memcache;

import hierophant.memcache.command.AddCommand;
import hierophant.memcache.command.Command;
import hierophant.memcache.command.RetrievalCommand;
import hierophant.memcache.command.SetCommand;
import hierophant.memcache.command.StorageCommand;
import hierophant.memcache.jjt.MemcacheParser;
import hierophant.memcache.jjt.ParseException;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

public class CommandTest {

    @Test
    public void getCommand(){
        StringReader reader = new StringReader("get hoge\r\n");
        MemcacheParser parser = new MemcacheParser(reader);
        try {
            Command command = parser.Command();
            RetrievalCommand ret = (RetrievalCommand) command;
            Assert.assertEquals(1, ret.getKeys().size());
            Assert.assertEquals("hoge", ret.getKeys().get(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void getsCommand(){
        StringReader reader = new StringReader("gets hoge foo\r\n");
        MemcacheParser parser = new MemcacheParser(reader);
        try {
            Command command = parser.Command();
            RetrievalCommand ret = (RetrievalCommand) command;
            Assert.assertEquals(2, ret.getKeys().size());
            Assert.assertEquals("hoge", ret.getKeys().get(0));
            Assert.assertEquals("foo", ret.getKeys().get(1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void setCommand(){
        StringReader reader = new StringReader("set xyzkey 0 0 6\r\n");
        MemcacheParser parser = new MemcacheParser(reader);
        try {
            Command command = parser.Command();
            StorageCommand storage = (StorageCommand) command;
            Assert.assertTrue(storage instanceof SetCommand);
            Assert.assertEquals("xyzkey", storage.getKey());
            Assert.assertEquals(0L, storage.getFlags());
            Assert.assertEquals(0L, storage.getExpTime());
            Assert.assertEquals(6L, storage.getLength());
            Assert.assertEquals(Boolean.FALSE, storage.getNoreply());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void setCommand_ext(){
        StringReader reader = new StringReader("set xyzkey 0 10 6 noreply\r\n");
        MemcacheParser parser = new MemcacheParser(reader);
        try {
            Command command = parser.Command();
            StorageCommand storage = (StorageCommand) command;
            Assert.assertTrue(storage instanceof SetCommand);
            Assert.assertEquals("xyzkey", storage.getKey());
            Assert.assertEquals(0L, storage.getFlags());
            Assert.assertEquals(10L, storage.getExpTime());
            Assert.assertEquals(6L, storage.getLength());
            Assert.assertEquals(Boolean.TRUE, storage.getNoreply());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void addCommand(){
        StringReader reader = new StringReader("add xyzkey 1 12 6\r\n");
        MemcacheParser parser = new MemcacheParser(reader);
        try {
            Command command = parser.Command();
            StorageCommand storage = (StorageCommand) command;
            Assert.assertTrue(storage instanceof AddCommand);
            Assert.assertEquals("xyzkey", storage.getKey());
            Assert.assertEquals(1L, storage.getFlags());
            Assert.assertEquals(12L, storage.getExpTime());
            Assert.assertEquals(6L, storage.getLength());
            Assert.assertEquals(Boolean.FALSE, storage.getNoreply());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
