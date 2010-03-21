package hierophant.core;

public enum Port {
    MemcacheServer(21217),
    QueueServer(21219),
    IndexServer(21211),
    Storage(21210)
    ;
    
    private final int port;
    private Port(final int port){
        this.port = port;
    }
    public int getPort(){
        return port;
    }
}
