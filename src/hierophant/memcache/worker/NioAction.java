package hierophant.memcache.worker;

import java.nio.channels.SelectionKey;

public interface NioAction {
    public void execute(SelectionKey selectionKey);
}
