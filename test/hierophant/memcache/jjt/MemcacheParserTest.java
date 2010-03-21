package hierophant.memcache.jjt;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

public class MemcacheParserTest {
    @Test
    public void emptyConstruct(){
        try {
            new MemcacheParser(new StringReader(""));
        } catch(Exception e){
            Assert.fail("空であってもエラーにはならない");
        }
    }
    
    @Test
    public void emptyTokenManager(){
        try {
            MemcacheParserTokenManager tm = new MemcacheParserTokenManager(null);
            new MemcacheParser(tm);
        } catch(Exception e){
            Assert.fail(e.getMessage());
        }
    }

    @Test(expected=NullPointerException.class)
    public void nullConstructor(){
        new MemcacheParser((InputStream) null);
    }
    
    @Test
    public void nullInputStream(){
        InputStream s = new InputStream(){
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        try {
            new MemcacheParser(s);
        } catch(Exception e){
            Assert.fail(e.getMessage());
        }
    }
}
