package hierophant.protobuf.storage;

import hierophant.protobuf.Procedure;
import hierophant.protobuf.RendezvousCommunicator;
import hierophant.protobuf.Responder;
import hierophant.protobuf.storage.Storage.Request;
import hierophant.protobuf.storage.Storage.Response;
import hierophant.protobuf.storage.Storage.StorageService;

import com.google.protobuf.Message;
import com.googlecode.protobuf.socketrpc.SocketRpcChannel;
import com.googlecode.protobuf.socketrpc.SocketRpcController;

class StorageCommunicatorFactory  {
    
    protected final StorageService service;
    
    public StorageCommunicatorFactory(final SocketRpcChannel channel){
        this.service = StorageService.newStub(channel);
    }
    
    public SetCommunicator createSet(){
        return new SetCommunicator();
    }
    
    public GetCommunicator createGet(){
        return new GetCommunicator();
    }
    
    public FlushCommunicator createFlush(){
        return new FlushCommunicator();
    }
    
    public RemoveCommunicator createRemove(){
        return new RemoveCommunicator();
    }
    
    protected class SetCommunicator extends RendezvousCommunicator<Response.Set> {
        public Response.Set communicate(SocketRpcController controller, Message request){
            return syncCall(new SetProcedureCall(), controller, request);
        }
    }
    
    protected class SetProcedureCall implements Procedure<Response.Set> {
        public void call(SocketRpcController controller, Message request, Responder<Response.Set> responder){
            service.set(controller, (Request.Set) request, responder);
        }
    }
    
    protected class GetCommunicator extends RendezvousCommunicator<Response.Get> {
        public Response.Get communicate(SocketRpcController controller, Message request){
            return syncCall(new GetProcedureCall(), controller, request);
        }
    }
    
    protected class GetProcedureCall implements Procedure<Response.Get> {
        public void call(SocketRpcController controller, Message request, Responder<Response.Get> responder){
            service.get(controller, (Request.Get) request, responder);
        }
    }

    protected class FlushCommunicator extends RendezvousCommunicator<Response.Flush> {
        public Response.Flush communicate(SocketRpcController controller, Message request){
            return syncCall(new FlushProcedureCall(), controller, request);
        }
    }
    
    protected class FlushProcedureCall implements Procedure<Response.Flush> {
        public void call(SocketRpcController controller, Message request, Responder<Response.Flush> responder){
            service.flush(controller, (Request.Flush) request, responder);
        }
    }

    protected class RemoveCommunicator extends RendezvousCommunicator<Response.Remove> {
        public Response.Remove communicate(SocketRpcController controller, Message request){
            return syncCall(new RemoveProcedureCall(), controller, request);
        }
    }
    
    protected class RemoveProcedureCall implements Procedure<Response.Remove> {
        public void call(SocketRpcController controller, Message request, Responder<Response.Remove> responder){
            service.remove(controller, (Request.Remove) request, responder);
        }
    }
}
