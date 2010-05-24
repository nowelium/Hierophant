package hierophant.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ThreadLocalMap<K, V> extends ThreadLocal<Map<K, V>> implements Map<K, V> {
    
    @Override
    protected Map<K, V> initialValue() {
        return new HashMap<K, V>();
    }

    public void clear() {
        get().clear();
    }

    public boolean containsKey(Object key) {
        return get().containsKey(key);
    }

    public boolean containsValue(Object value) {
        return get().containsValue(value);
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return get().entrySet();
    }

    public V get(Object key) {
        return get().get(key);
    }

    public boolean isEmpty() {
        return get().isEmpty();
    }

    public Set<K> keySet() {
        return get().keySet();
    }

    public V put(K key, V value) {
        return get().put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> t) {
        get().putAll(t);
    }

    public V remove(Object key) {
        return get().remove(key);
    }

    public int size() {
        return get().size();
    }

    public Collection<V> values() {
        return get().values();
    }

}

