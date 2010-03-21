package hierophant.core.hash.impl;

import hierophant.core.hash.HashFunction;

import org.junit.Assert;
import org.junit.Test;

public class HashMD5FunctionTest {

    @Test
    public void testHash() {
        HashFunction func = new HashMD5Function();
        System.out.println("hoge = \t" + func.hash("hoge"));
        System.out.println("foo  = \t" + func.hash("foo"));
        System.out.println("bar  = \t" + func.hash("bar"));
        Assert.assertTrue(func.hash("hoge") == func.hash("hoge"));
        Assert.assertTrue(func.hash("foo") == func.hash("foo"));
        Assert.assertFalse(func.hash("hoge") == func.hash("foo"));
    }

}
