package hierophant.core.hash.impl;

import hierophant.core.hash.HashFunction;
import hierophant.core.util.ThreadLocalMap;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum Hash implements HashFunction {
    
    MD5("MD5", 7),
    SHA1("SHA1", 7)
    ;
    
    protected static final Charset charset = Charset.forName("UTF-8");
    
    protected static final ThreadLocalMap<String, MessageDigest> digests = new ThreadLocalMap<String, MessageDigest>();
    
    private final String algorithm;
    
    private final int hashSize;
    
    private Hash(String algorithm, int hashSize){
        this.algorithm = algorithm;
        this.hashSize = hashSize;
    }
    
    protected static MessageDigest getInstance(String algorithm) {
        if(digests.containsKey(algorithm)){
            return digests.get(algorithm);
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digests.put(algorithm, digest);
            return digest;
        } catch(NoSuchAlgorithmException e){
            throw new RuntimeException(e);
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
    
    protected long hash(final String algorithm, final String key, final int maxSize){
        return hash(getInstance(algorithm), key, maxSize);
    }
    
    protected long hash(final MessageDigest digest, final String key, final int maxSize){
        synchronized(this){
            digest.reset();
            digest.update(charset.encode(key));
            return hash(digest.digest(), maxSize);
        }
    }
    
    public long hash(String key){
        return hash(algorithm, key, hashSize);
    }

}
