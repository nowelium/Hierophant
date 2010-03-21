package hierophant.protobuf;

import com.google.protobuf.Message;
import com.googlecode.protobuf.socketrpc.SocketRpcController;

public interface Procedure<N> {
    public void call(SocketRpcController controller, Message request, Responder<N> responder);
}
