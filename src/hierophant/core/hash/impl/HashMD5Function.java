package hierophant.core.hash.impl;

import java.security.MessageDigest;

public class HashMD5Function extends AbstractMessageDigestFunction {
    
    private static final int HASH_SIZE = 7;
    
    private static final String ALGO = "MD5";
    
    private final MessageDigest digest = getInstance(ALGO);
    
    public long hash(String key){
        return hash(digest, key, HASH_SIZE);
    }
}
