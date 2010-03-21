package hierophant.core.hash;

import java.util.List;

public interface HashContainer<E> {
    
    public void add(E e);
    
    public void remove(E e);
    
    public E get(String key);

    public List<E> values();
}
