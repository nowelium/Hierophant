package hierophant.core.hash.impl;

import hierophant.core.hash.HashFunction;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

abstract class AbstractMessageDigestFunction implements HashFunction {
    
    protected static Map<String, MessageDigest> cache = new ConcurrentHashMap<String, MessageDigest>();
    
    protected static final Charset charset = Charset.forName("UTF-8");
    
    protected static MessageDigest getInstance(String key) {
        synchronized(cache){
            if(cache.containsKey(key)){
                return cache.get(key);
            }
            try {
                MessageDigest digest = MessageDigest.getInstance(key);
                cache.put(key, digest);
                return digest;
            } catch(NoSuchAlgorithmException e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
    
    protected long hash(final byte[] hash, final int maxSize){
        long temp = 0L;
        long value = 0L;
        for(short i = 0; i < maxSize; ++i){
            temp = hash[i] & 0xff;
            value |= temp << ((maxSize - 1 - i) * 8);
        }
        value |= temp;
        return value;
    }
    
    protected long hash(final MessageDigest digest, final String key, final int maxSize){
        synchronized(this){
            digest.reset();
            digest.update(charset.encode(key));
            return hash(digest.digest(), maxSize);
        }
    }

}
