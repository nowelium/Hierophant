package hierophant.memcache;

import hierophant.memcache.Return.ResponseType;

import org.junit.Assert;
import org.junit.Test;

public class ReturnTest {

    @Test
    public void SERVER_ERROR(){
        Return r = new Return(ResponseType.SERVER_ERROR, "hello world");
        System.out.println(r);
        Assert.assertEquals("SERVER_ERROR\nhello world", r.renderMessage());
    }
    
    @Test
    public void CLIENT_ERROR(){
        Return r = new Return(ResponseType.CLIENT_ERROR, "hello world");
        System.out.println(r);
        Assert.assertEquals("CLIENT_ERROR\nhello world", r.renderMessage());
    }
    
    @Test
    public void SEND_VALUE(){
        Return r = new Return(ResponseType.SEND_VALUE, 1, 2, 3);
        System.out.println(r);
        Assert.assertEquals("VALUE 1 2 3", r.renderMessage());
    }
    
    @Test
    public void STORED(){
        Return r = new Return(ResponseType.STORED);
        System.out.println(r);
        Assert.assertEquals("STORED", r.renderMessage());
    }
    
    @Test
    public void NOT_STORED(){
        Return r = new Return(ResponseType.NOT_STORED);
        System.out.println(r);
        Assert.assertEquals("NOT_STORED", r.renderMessage());
    }
    
    @Test
    public void EXISTS(){
        Return r = new Return(ResponseType.EXISTS);
        System.out.println(r);
        Assert.assertEquals("EXISTS", r.renderMessage());
    }
    
    @Test
    public void NOT_FOUND(){
        Return r = new Return(ResponseType.NOT_FOUND);
        System.out.println(r);
        Assert.assertEquals("NOT_FOUND", r.renderMessage());
    }
    
    @Test
    public void ERROR(){
        Return r = new Return(ResponseType.ERROR);
        System.out.println(r);
        Assert.assertEquals("ERROR", r.renderMessage());
    }
    
    @Test
    public void DELETED(){
        Return r = new Return(ResponseType.DELETED);
        System.out.println(r);
        Assert.assertEquals("DELETED", r.renderMessage());
    }
    
    @Test
    public void END(){
        Return r = new Return(ResponseType.END);
        System.out.println(r);
        Assert.assertEquals("END", r.renderMessage());
    }
}
