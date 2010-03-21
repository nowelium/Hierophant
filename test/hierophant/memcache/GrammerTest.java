package hierophant.memcache;

import hierophant.memcache.jjt.MemcacheParser;
import hierophant.memcache.jjt.Token;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

public class GrammerTest {
    
    @Test
    public void getGrammer(){
        StringReader reader = new StringReader("gets hoge\r\n");
        MemcacheParser parser = new MemcacheParser(reader);
        {
            Token token = parser.getNextToken();
            System.out.println(token);
            Assert.assertEquals("gets", token.image);
        }
        {
            Token token = parser.getNextToken();
            System.out.println(token);
            Assert.assertEquals("hoge", token.image);
        }
        {
            Token token = parser.getNextToken();
            Assert.assertEquals("", token.image);
        }
    }
    @Test
    public void getsGrammer(){
        StringReader reader = new StringReader("gets hoge foo\r\n");
        MemcacheParser parser = new MemcacheParser(reader);
        {
            Token token = parser.getNextToken();
            System.out.println(token);
            Assert.assertEquals("gets", token.image);
        }
        {
            Token token = parser.getNextToken();
            System.out.println(token);
            Assert.assertEquals("hoge", token.image);
        }
        {
            Token token = parser.getNextToken();
            System.out.println(token);
            Assert.assertEquals("foo", token.image);
        }
        {
            Token token = parser.getNextToken();
            Assert.assertEquals("", token.image);
        }
    }
    @Test
    public void setGrammer(){
        StringReader reader = new StringReader("set hoge 0 0 4\r\n");
        MemcacheParser parser = new MemcacheParser(reader);
        {
            Token token = parser.getNextToken();
            System.out.println(token);
            Assert.assertEquals("set", token.image);
        }
        {
            Token token = parser.getNextToken();
            System.out.println(token);
            Assert.assertEquals("hoge", token.image);
        }
        {
            Token token = parser.getNextToken();
            Assert.assertEquals("0", token.image);
        }
        {
            Token token = parser.getNextToken();
            Assert.assertEquals("0", token.image);
        }
        {
            Token token = parser.getNextToken();
            Assert.assertEquals("4", token.image);
        }
        {
            Token token = parser.getNextToken();
            Assert.assertEquals("", token.image);
        }
    }
    @Test
    public void incrementGrammer(){
        StringReader reader = new StringReader("increment hoge 100\r\n");
        MemcacheParser parser = new MemcacheParser(reader);
        {
            Token token = parser.getNextToken();
            System.out.println(token);
            Assert.assertEquals("increment", token.image);
        }
        {
            Token token = parser.getNextToken();
            System.out.println(token);
            Assert.assertEquals("hoge", token.image);
        }
        {
            Token token = parser.getNextToken();
            Assert.assertEquals("100", token.image);
        }
        {
            Token token = parser.getNextToken();
            Assert.assertEquals("", token.image);
        }
    }
}
