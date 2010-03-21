package hierophant.protobuf;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;

import com.google.protobuf.Message;
import com.googlecode.protobuf.socketrpc.SocketRpcController;

public abstract class RendezvousCommunicator<E> implements Communicator<E> {
    
    protected final ExecutorService executor = Executors.newCachedThreadPool();
    
    protected void asyncCall(Procedure<E> proc, SocketRpcController controller, Message request){
        final DefaultResponder<E> responder = new DefaultResponder<E>();
        proc.call(controller, request, responder);
    }
    
    protected E syncCall(Procedure<E> proc, SocketRpcController controller, Message request) {
        final Rendezvous<E> responder = new Rendezvous<E>();
        try {
            Future<E> response = executor.submit(responder);
            proc.call(controller, request, responder);
            return response.get();
        } catch(InterruptedException e){
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    protected static class DefaultResponder<E> implements Responder<E> {
        public void run(E response) {
            // nop
        }
    }
    
    protected static class Rendezvous<E> implements Responder<E>, Callable<E> {
        protected final SynchronousQueue<E> queue = new SynchronousQueue<E>();
        public E call() throws Exception {
            // randezvous
            return queue.take();
        }
        public void run(E response) {
            // release!
            queue.offer(response);
        }
    }
}
