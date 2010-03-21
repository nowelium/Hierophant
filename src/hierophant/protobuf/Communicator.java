package hierophant.protobuf;

import com.google.protobuf.Message;
import com.googlecode.protobuf.socketrpc.SocketRpcController;

public interface Communicator<E> {
    public E communicate(SocketRpcController controller, Message request);
}
