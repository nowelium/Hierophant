package hierophant.protobuf.index;

import hierophant.protobuf.index.Index.IndexResponse.GetNodeList;
import hierophant.protobuf.index.Index.IndexResponse.GetOperation;
import hierophant.protobuf.index.Index.IndexResponse.SetStatus;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;

public class IndexReceiver implements Index.IndexService.BlockingInterface {

    public GetNodeList getNodeList(RpcController controller, Index.IndexRequest.GetNodeList request) throws ServiceException {
        return null;
    }

    public GetOperation getOperation(RpcController controller, Index.IndexRequest.GetOperation request) throws ServiceException {
        return null;
    }

    public SetStatus setStatus(RpcController controller, Index.IndexRequest.SetStatus request) throws ServiceException {
        return null;
    }
}
