package hierophant.core.hash.impl;

import java.security.MessageDigest;

public class HashSHA1Function extends AbstractMessageDigestFunction {
    
    private static final int HASH_SIZE = 7;
    
    private static final String ALGO = "SHA1";

    private final MessageDigest digest = getInstance(ALGO);
    
    public long hash(String key){
        return hash(digest, key, HASH_SIZE);
    }
}
