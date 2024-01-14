package cn.lhx.study.repo_management.service_layer;
import cn.lhx.study.repo_management.entities.Commodity;
import cn.lhx.study.repo_management.entities.Page;
import cn.lhx.study.repo_management.entities.ProcessingResult;

import java.util.List;

public interface InterCommodityService{
    //service直接返回一个页对象
    Page<Commodity> selectByKeyword(String keyword,int currentPageNumber,int pageSize);
    ProcessingResult delete(String cid);
    Commodity selectById(int cid);

    ProcessingResult modifyService(String cid,String name,String unit_price);

    ProcessingResult insertService(String name,String unit_price);
}
