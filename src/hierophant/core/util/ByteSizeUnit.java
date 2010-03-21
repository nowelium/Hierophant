package hierophant.core.util;

public enum ByteSizeUnit {
    
    Killo(1024),
    Mega(1024 * 1024),
    Giga(1024 * 1024 * 1024)
    ;
    
    private final long allocate;
    private ByteSizeUnit(final long allocate){
        this.allocate = allocate;
    }
    
    public long toLong(long targetSize){
        return targetSize * allocate;
    }

}
