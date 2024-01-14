package cn.lhx.study.repo_management.service_layer;

import cn.lhx.study.repo_management.entities.Batch;
import cn.lhx.study.repo_management.entities.Page;
import cn.lhx.study.repo_management.entities.ProcessingResult;

public interface InterBatchService {
    Page<Batch> queryByCommodityId(String cid, int currentPageNumber, int pageSize);
    Page<Batch> queryByWarehouseId(String wid, int currentPageNumber, int pageSize);
    Page<Batch> queryByQuantityRange(String from, String to, int currentPageNumber, int pageSize);
    ProcessingResult insert(String cid, String wid, String quantity);
    ProcessingResult deleteService(String cid, String wid);
    ProcessingResult inventoryOut(String source_wid,String dest_wid,String cid,String transferQuantity) throws Exception;
    ProcessingResult inventoryIn(String source_wid,String dest_wid,String cid,String transferQuantity) throws Exception;
}
