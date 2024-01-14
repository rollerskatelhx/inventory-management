package cn.lhx.study.repo_management.service_layer;
import cn.lhx.study.repo_management.entities.Page;
import cn.lhx.study.repo_management.entities.ProcessingResult;
import cn.lhx.study.repo_management.entities.Warehouse;

public interface InterWarehouseService {
    Page<Warehouse> selectByAddrKeyword(String keyword, int currentPageNumber, int pageSize);

    Warehouse selectById(String wid);

    ProcessingResult deleteService(String wid);

    ProcessingResult insertService(String addr);

    ProcessingResult modifyService(String wid,String addr);
}
