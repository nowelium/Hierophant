package hierophant.core.io.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hierophant.core.io.Reader;

public class StreamReader implements Reader {
    
    protected final BufferedReader reader;
    
    public StreamReader(final InputStreamReader reader){
        this.reader = new BufferedReader(reader);
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public boolean readable() throws IOException {
        return reader.ready();
    }

}
