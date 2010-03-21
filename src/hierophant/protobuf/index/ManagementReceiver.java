package hierophant.protobuf.index;

import hierophant.protobuf.index.Index.ManagementResponse.AddGroup;
import hierophant.protobuf.index.Index.ManagementResponse.AddNode;
import hierophant.protobuf.index.Index.ManagementResponse.GetNodeList;
import hierophant.protobuf.index.Index.ManagementResponse.NodeStatus;
import hierophant.protobuf.index.Index.ManagementResponse.Proxy;
import hierophant.protobuf.index.Index.ManagementResponse.RemoveGroup;
import hierophant.protobuf.index.Index.ManagementResponse.RemoveNode;
import hierophant.protobuf.index.Index.ManagementResponse.Replica;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;

public class ManagementReceiver implements Index.ManagementService.BlockingInterface {

    public AddGroup addGroup(RpcController controller, Index.ManagementRequest.AddGroup request) throws ServiceException {
        return null;
    }

    public RemoveGroup removeGroup(RpcController controller, Index.ManagementRequest.RemoveGroup request) throws ServiceException {
        return null;
    }
    
    public AddNode addNode(RpcController controller, Index.ManagementRequest.AddNode request) throws ServiceException {
        return null;
    }
    
    public RemoveNode removeNode(RpcController controller, Index.ManagementRequest.RemoveNode request) throws ServiceException {
        return null;
    }

    public GetNodeList getNodeList(RpcController controller, Index.ManagementRequest.GetNodeList request) throws ServiceException {
        return null;
    }

    public NodeStatus nodeStatus(RpcController controller, Index.ManagementRequest.NodeStatus request) throws ServiceException {
        return null;
    }

    public Replica replica(RpcController controller, Index.ManagementRequest.Replica request) throws ServiceException {
        throw new ServiceException("not yet implemented");
    }

    public Proxy proxy(RpcController controller, Index.ManagementRequest.Proxy request) throws ServiceException {
        throw new ServiceException("not yet implemented");
    }

}
