package hierophant.core.node.impl;

import hierophant.core.node.StorageNode;

public class BlackholeStorageNode<K, V> extends StorageNode<K, V> {

    @Override
    public void flush(long expiredAt) {
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void remove(K key, long expiredAt) {
    }

    @Override
    public boolean set(K key, V value) {
        return false;
    }

    public String getUnique() {
        return null;
    }

}
