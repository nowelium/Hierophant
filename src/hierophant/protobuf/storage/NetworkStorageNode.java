package hierophant.protobuf.storage;

import hierophant.core.cache.Cache.Entry;
import hierophant.core.node.StorageNode;
import hierophant.protobuf.storage.Storage.Request;
import hierophant.protobuf.storage.Storage.Response;
import hierophant.protobuf.storage.Storage.Store;
import hierophant.protobuf.storage.Storage.Store.Key;
import hierophant.protobuf.storage.Storage.Store.Value;
import hierophant.protobuf.storage.StorageCommunicatorFactory.FlushCommunicator;
import hierophant.protobuf.storage.StorageCommunicatorFactory.GetCommunicator;
import hierophant.protobuf.storage.StorageCommunicatorFactory.RemoveCommunicator;
import hierophant.protobuf.storage.StorageCommunicatorFactory.SetCommunicator;

import com.google.protobuf.ByteString;
import com.googlecode.protobuf.socketrpc.SocketRpcChannel;
import com.googlecode.protobuf.socketrpc.SocketRpcController;

public class NetworkStorageNode extends StorageNode<String, Entry<String, String>> {
    
    protected final String unique;
    
    protected final SocketRpcChannel channel;
    
    protected final SocketRpcController controller;
    
    protected final SetCommunicator set;
    
    protected final GetCommunicator get;
    
    protected final FlushCommunicator flush;
    
    protected final RemoveCommunicator remove;
    
    public NetworkStorageNode(final String ipAddr, final int port){
        this(ipAddr, ipAddr, port);
    }
    
    public NetworkStorageNode(final String unique, final String ipAddr, final int port){
        this.unique = unique;
        this.channel = new SocketRpcChannel(ipAddr, port);
        this.controller = channel.newRpcController();
        
        final StorageCommunicatorFactory factory = new StorageCommunicatorFactory(channel);
        this.set = factory.createSet();
        this.get = factory.createGet();
        this.flush = factory.createFlush();
        this.remove = factory.createRemove();
    }
    
    public String getUnique(){
        return unique;
    }
    
    protected static Value convertEntryToValue(Entry<String, String> entry){
        final String value = entry.getValue();
        Value.Builder builder = Value.newBuilder();
        builder.setExpiredAtTstmp(entry.getExpiredAt());
        builder.setValue(ByteString.copyFrom(value.getBytes()));
        builder.setFlags(Long.valueOf(entry.getFlag()).intValue());
        builder.setSize(value.length());
        return builder.build();
    }
    
    protected static Key convertKey(String key){
        Key.Builder builder = Key.newBuilder();
        builder.setKey(key);
        return builder.build();
    }

    @Override
    public boolean set(String key, Entry<String, String> entry) {
        final Store.Builder sb = Store.newBuilder();
        sb.setKey(convertKey(key));
        sb.setValue(convertEntryToValue(entry));
        
        final Request.Set.Builder builder = Request.Set.newBuilder();
        builder.setStore(sb.build());
        
        Response.Set response = set.communicate(controller, builder.build());
        if(null == response){
            return false;
        }
        return response.getSucceed();
    }
    
    @Override
    public Entry<String, String> get(String key) {
        final Request.Get.Builder builder = Request.Get.newBuilder();
        builder.setKey(convertKey(key));
        
        Response.Get response = get.communicate(controller, builder.build());
        if(null == response){
            return null;
        }
        Store store = response.getStore();
        Store.Key sKey = store.getKey();
        Store.Value sValue = store.getValue();
        return new ResponsEntry(
            sKey.getKey(),
            sValue.getValue().toStringUtf8(),
            sValue.getFlags(),
            sValue.getExpiredAtTstmp()
        );
    }

    @Override
    public void remove(String key, long expiredAt) {
        final Request.Remove.Builder builder = Request.Remove.newBuilder();
        builder.setKey(convertKey(key));
        builder.setExclutionAtTstmp(expiredAt);
        
        remove.communicate(controller, builder.build());
    }

    @Override
    public void flush(long expiredAt) {
        final Request.Flush.Builder builder = Request.Flush.newBuilder();
        builder.setExclutionAtTstmp(expiredAt);
        
        flush.communicate(controller, builder.build());
    }
    
    protected static class ResponsEntry implements Entry<String, String> {
        protected final String key;
        protected final String value;
        protected final long flag;
        protected final long expiredAt;
        protected ResponsEntry(final String key, final String value, final long flag, final long expiredAt){
            this.key = key;
            this.value = value;
            this.flag = flag;
            this.expiredAt = expiredAt;
        }
        public long getExpiredAt() {
            return expiredAt;
        }
        public long getFlag() {
            return flag;
        }
        public String getKey() {
            return key;
        }
        public String getValue() {
            return value;
        }
    }
    
}
