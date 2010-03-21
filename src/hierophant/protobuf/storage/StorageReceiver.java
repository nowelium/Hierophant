package hierophant.protobuf.storage;

import hierophant.core.cache.Cache;
import hierophant.core.cache.Cache.Entry;
import hierophant.protobuf.storage.Storage.StorageService;
import hierophant.protobuf.storage.Storage.Store;
import hierophant.protobuf.storage.Storage.Response.Flush;
import hierophant.protobuf.storage.Storage.Response.Get;
import hierophant.protobuf.storage.Storage.Response.Remove;
import hierophant.protobuf.storage.Storage.Response.Set;
import hierophant.protobuf.storage.Storage.Store.Key;
import hierophant.protobuf.storage.Storage.Store.Value;

import com.google.protobuf.BlockingService;
import com.google.protobuf.ByteString;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;

public class StorageReceiver implements Storage.StorageService.BlockingInterface {
    
    protected final Cache<String, Entry<String, String>> cache;
    
    public StorageReceiver(Cache<String, Entry<String, String>> cache){
        this.cache = cache;
    }
    
    public BlockingService createBlockingService(){
        return StorageService.newReflectiveBlockingService(this);
    }
    
    protected static Value convertEntryToValue(Entry<String, String> entry){
        Value.Builder builder = Value.newBuilder();
        builder.setExpiredAtTstmp(entry.getExpiredAt());
        builder.setValue(ByteString.copyFrom(entry.getValue().getBytes()));
        builder.setFlags(Long.valueOf(entry.getFlag()).intValue());
        return builder.build();
    }
    
    protected static Key convertKey(String key){
        Key.Builder builder = Key.newBuilder();
        builder.setKey(key);
        return builder.build();
    }

    public Flush flush(RpcController controller, Storage.Request.Flush request) throws ServiceException {
        cache.flush(request.getExclutionAtTstmp());
        return Flush.newBuilder().build();
    }

    public Remove remove(RpcController controller, Storage.Request.Remove request) throws ServiceException {
        cache.remove(request.getKey().getKey(), request.getExclutionAtTstmp());
        return Remove.newBuilder().build();
    }
    
    public Get get(RpcController controller, Storage.Request.Get request) throws ServiceException {
        Entry<String, String> entry = cache.get(request.getKey().getKey());
        
        Store.Builder sb = Store.newBuilder();
        sb.setKey(convertKey(entry.getKey()));
        sb.setValue(convertEntryToValue(entry));
        
        Get.Builder builder = Get.newBuilder();
        builder.setStore(sb.build());
        return builder.build();
    }

    public Set set(RpcController controller, Storage.Request.Set request) throws ServiceException {
        Store store = request.getStore();
        Store.Key sKey = store.getKey();
        Store.Value sValue = store.getValue();
        ReceiveEntry entry = new ReceiveEntry(
            sKey.getKey(),
            sValue.getValue().toStringUtf8(),
            sValue.getFlags(),
            sValue.getExpiredAtTstmp()
        );
        boolean succeed = cache.set(sKey.getKey(), entry);
        return Set.newBuilder().setSucceed(succeed).build();
    }
    
    protected static class ReceiveEntry implements Entry<String, String> {
        protected final String key;
        protected final String value;
        protected final long flag;
        protected final long expiredAt;
        protected ReceiveEntry(final String key, final String value, final long flag, final long expiredAt){
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