package cn.lhx.study.repo_management.service_layer.impl;
import cn.lhx.study.repo_management.custom_configuration.CacheConfiguration;
import cn.lhx.study.repo_management.entities.Commodity;
import cn.lhx.study.repo_management.entities.Page;
import cn.lhx.study.repo_management.entities.ProcessingResult;
import cn.lhx.study.repo_management.entities.Warehouse;
import cn.lhx.study.repo_management.mappers.WarehouseMapper;
import cn.lhx.study.repo_management.service_layer.InterWarehouseService;
import cn.lhx.study.repo_management.wheel.CacheUtils;
import cn.lhx.study.repo_management.wheel.Frame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Service
public class WarehouseService implements InterWarehouseService {
    @Autowired
    private WarehouseMapper warehouseMapper;

    public Page<Warehouse> selectByAddrKeyword(String keyword, int currentPageNumber,int pageSize){
        String like="%"+keyword+"%";
        String cacheKey="warehouse"+like;
        Object cache= CacheUtils.get(cacheKey);
        Page<Warehouse> resultPage=null;
        if(cache==null){
            List<Warehouse> warehousesMeetCondition=warehouseMapper.selectByAddrLike(like);
            if(warehousesMeetCondition==null||warehousesMeetCondition.isEmpty()){
                return null;
            }
            Frame<Warehouse> frame=new Frame<>(warehousesMeetCondition,pageSize);
            List<Page<Warehouse>> pages=frame.GenerateMeetConditionPages();
            resultPage=pages.get(currentPageNumber-1);
            CacheUtils.put(cacheKey,pages, CacheConfiguration.EXPIRE_MILLISECONDS);
        }else{
            //有缓存
            System.out.println("缓存命中");
            List<Page<Warehouse>> pages=(List<Page<Warehouse>>) cache;
            resultPage=pages.get(currentPageNumber-1);
        }
        return resultPage;
    }

    public Warehouse selectById(String wid){
        //前端校验：wid是表示正整数的串
        return warehouseMapper.selectById(Integer.parseInt(wid));
    }

    public ProcessingResult deleteService(String wid){
        int wid_int=Integer.parseInt(wid);
        if(warehouseMapper.selectById(wid_int)==null){
            return new ProcessingResult(false,"目标记录查找不到");
        }
        int rowsAffected=warehouseMapper.delete(wid_int);
        if(rowsAffected<1){
            return new ProcessingResult(false,"删除失败");
        }else{
            deleteAllWarehousePagesCache();
            return new ProcessingResult(true,"删除成功");
        }
        //ok
    }

    public ProcessingResult insertService(String addr){
        //前端校验：addr不为空且不为空串
        ProcessingResult pr;
        Warehouse warehouseWithAddr=warehouseMapper.selectByAddr(addr);
        if(warehouseWithAddr!=null){
            return new ProcessingResult(false,"有相同地址的库房");
        }
        int rowsAffected=warehouseMapper.insert(addr);
        if(rowsAffected<1){
            return new ProcessingResult(false,"插入失败");
        }
        deleteAllWarehousePagesCache();
        return new ProcessingResult(true,"添加成功");
    }

    public ProcessingResult modifyService(String wid,String addr){
        //前台的校验保证了addr不为空且不为空串
        if(warehouseMapper.selectById(Integer.parseInt(wid))==null){
            return new ProcessingResult(false,"wid=x的记录查找不到");
        }

        Warehouse warehouseWithAddr=warehouseMapper.selectByAddr(addr);
        if(warehouseWithAddr!=null){
            return new ProcessingResult(false,"已有地址为为"+addr+"的库房，故修改失败");
        }
        int rowsAffected=warehouseMapper.modify(Integer.parseInt(wid),addr);
        if(rowsAffected>=1){
            deleteAllWarehousePagesCache();
            return new ProcessingResult(true,"修改成功");
        }else{
            return new ProcessingResult(false,"修改失败");
        }
    }

    private void deleteAllWarehousePagesCache(){
        Set<String> cacheKeys=CacheUtils.getAllCacheKeys();
        for (String cacheKey : cacheKeys) {
            if(cacheKey.contains("warehouse")){
                CacheUtils.remove(cacheKey);
            }
        }
    }
}
