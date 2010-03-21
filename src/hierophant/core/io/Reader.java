package hierophant.core.io;

import java.io.IOException;

public interface Reader {
    
    public String readLine() throws IOException;
    
    public boolean readable() throws IOException;
}
