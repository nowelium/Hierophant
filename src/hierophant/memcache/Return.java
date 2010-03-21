package hierophant.memcache;

import java.util.Formatter;
import java.util.Locale;

public class Return {
    private static final String CRLF = "\r\n";
    public static enum ResponseType {
        STORED,
        NOT_STORED,
        EXISTS,
        NOT_FOUND,
        ERROR,
        CLIENT_ERROR("CLIENT_ERROR\r\n%s"),
        SERVER_ERROR("SERVER_ERROR\r\n%s"),
        SEND_VALUE("VALUE %s %d %d\r\n%s\r\nEND"),
        DELETED,
        END
        ;
        private final String messageFormat;
        private ResponseType(){
            this.messageFormat = name();
        }
        private ResponseType(String messageFormat){
            this.messageFormat = messageFormat;
        }
        public String getMessageFormat(){
            return messageFormat;
        }
    }
    
    private ResponseType type;
    
    private Object[] params;
    
    private final Return[] returns;
    
    public Return(final ResponseType type, final Object...params){
        this.type = type;
        this.params = params;
        this.returns = new Return[]{this};
    }
    public Return(final Return...returns){
        this.returns = returns;
    }
    
    public String renderMessage(){
        StringBuilder buf = new StringBuilder();
        for(Return r: returns){
            Formatter formatter = new Formatter(buf, Locale.US);
            formatter.format(r.type.messageFormat, r.params);
            buf.append(CRLF);
        }
        return buf.toString();
    }
    
}
