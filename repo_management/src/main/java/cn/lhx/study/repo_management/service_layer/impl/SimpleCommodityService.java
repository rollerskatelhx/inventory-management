package cn.lhx.study.repo_management.service_layer.impl;
import cn.lhx.study.repo_management.custom_configuration.CacheConfiguration;
import cn.lhx.study.repo_management.entities.Batch;
import cn.lhx.study.repo_management.entities.Commodity;
import cn.lhx.study.repo_management.entities.Page;
import cn.lhx.study.repo_management.entities.ProcessingResult;
import cn.lhx.study.repo_management.mappers.BatchMapper;
import cn.lhx.study.repo_management.mappers.CommodityMapper;
import cn.lhx.study.repo_management.service_layer.InterCommodityService;
import cn.lhx.study.repo_management.wheel.CacheUtils;
import cn.lhx.study.repo_management.wheel.Frame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Service
public class SimpleCommodityService implements InterCommodityService {
    @Autowired
    private CommodityMapper cm;

    @Autowired
    private BatchMapper bm;
    @Override
    public Page<Commodity> selectByKeyword(String keyword, int currentPageNumber,int pageSize){
        String like="%"+keyword+"%";
        String cacheKey="commodity"+like;
        Object cache=CacheUtils.get(cacheKey);
        Page<Commodity> resultPage=null;
        if(cache==null){
            List<Commodity> commoditiesMeetCondition=cm.selectByNameLike(like);
            if(commoditiesMeetCondition==null||commoditiesMeetCondition.isEmpty()){
                return null;
            }
            Frame<Commodity> frame=new Frame<>(commoditiesMeetCondition,pageSize);
            List<Page<Commodity>> pages=frame.GenerateMeetConditionPages();
            resultPage=pages.get(currentPageNumber-1);
            CacheUtils.put(cacheKey,pages, CacheConfiguration.EXPIRE_MILLISECONDS);
        }else{
            //有缓存
            System.out.println("缓存命中");
            List<Page<Commodity>> pages=(List<Page<Commodity>>) cache;
            resultPage=pages.get(currentPageNumber-1);
        }
        return resultPage;
    }

    public ProcessingResult delete(String cid){
        if(cid==null||cid.equals("")){
            return new ProcessingResult(false,"cid==null");
        }
        if(cm.selectById(Integer.parseInt(cid))==null){
            return new ProcessingResult(false,"cid=x的记录查找不到");
        }
        List<Batch> resultBatches=bm.selectByCommodityId(Integer.parseInt(cid));
        if(resultBatches!=null&&!resultBatches.isEmpty()){
            //说明batch表中存在cid=x的记录，则不应该删除com表中cid=x的记录。
            ProcessingResult pr=new ProcessingResult(false,"batch表有cid=x的记录，不应删除货种表中cid=x的主键");
            return pr;
        }
        //可删
        cm.delete(Integer.parseInt(cid));
        ProcessingResult pr=new ProcessingResult(true,"删除com表中cid=x的记录成功");
        deleteAllCommodityPagesCache();
        return pr;
    }

    @Override
    public Commodity selectById(int cid) {
        return cm.selectById(cid);
    }

    @Override
    public ProcessingResult modifyService(String cid, String name, String unit_price){
        //前台的校验保证了name和unit_price不可能皆空。
        if(cid==null||cid.equals("")){
            return new ProcessingResult(false,"cid==null");
        }
        if(cm.selectById(Integer.parseInt(cid))==null){
            return new ProcessingResult(false,"cid=x的记录查找不到");
        }

        Commodity commodityWithNameSpecified=cm.selectByName(name);
        if(commodityWithNameSpecified!=null){
            return new ProcessingResult(false,"已有名为"+name+"的商品，故修改失败");
        }
        int unit_price_int;
        if(unit_price==null||unit_price.equals("")){
            unit_price_int=-1;
        }else{
            unit_price_int=Integer.parseInt(unit_price);
        }
        int rowsAffected=cm.modify(Integer.parseInt(cid),name,unit_price_int);
        if(rowsAffected>=1){
            deleteAllCommodityPagesCache();
            return new ProcessingResult(true,"修改成功");
        }else{
            return new ProcessingResult(false,"修改失败");
        }
    }

    @Override
    public ProcessingResult insertService(String name, String unit_price) {
        //前台校验保证name和price皆非空且price是范围正确的正整数
        ProcessingResult pr;
        Commodity commodityWithName=cm.selectByName(name);
        if(commodityWithName!=null){
            return new ProcessingResult(false,"有重名货品");
        }
        try{
            cm.insert(name,Integer.parseInt(unit_price));
        }catch(Exception e){
            pr=new ProcessingResult(false,"exception");
            return pr;
        }
        deleteAllCommodityPagesCache();
        return new ProcessingResult(true,"添加成功");
    }

    private void deleteAllCommodityPagesCache(){
        Set<String> cacheKeys=CacheUtils.getAllCacheKeys();
        for (String cacheKey : cacheKeys) {
            if(cacheKey.contains("commodity")){
                CacheUtils.remove(cacheKey);
            }
        }
    }

}
