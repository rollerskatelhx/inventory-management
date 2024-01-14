package cn.lhx.study.repo_management.service_layer.impl;
import cn.lhx.study.repo_management.custom_configuration.CacheConfiguration;
import cn.lhx.study.repo_management.entities.Batch;
import cn.lhx.study.repo_management.entities.Commodity;
import cn.lhx.study.repo_management.entities.Page;
import cn.lhx.study.repo_management.entities.ProcessingResult;
import cn.lhx.study.repo_management.mappers.BatchMapper;
import cn.lhx.study.repo_management.service_layer.InterBatchService;
import cn.lhx.study.repo_management.wheel.CacheUtils;
import cn.lhx.study.repo_management.wheel.Frame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class BatchService implements InterBatchService {
    @Autowired
    private BatchMapper bm;

    public Page<Batch> queryByCommodityId(String cid, int currentPageNumber, int pageSize){
        //前端已校验过cid是表示数字的非空字符串，且为一个正整数
        String cacheKey="batch"+"cid"+cid;
        Object cache= CacheUtils.get(cacheKey);
        Page<Batch> resultPage=null;
        if(cache==null){
            List<Batch> batchesMeetCondition=bm.selectByCommodityId(Integer.parseInt(cid));
            if(batchesMeetCondition==null||batchesMeetCondition.isEmpty()){
                return null;
            }
            Frame<Batch> frame=new Frame<>(batchesMeetCondition,pageSize);
            List<Page<Batch>> pages=frame.GenerateMeetConditionPages();
            resultPage=pages.get(currentPageNumber-1);
            CacheUtils.put(cacheKey,pages, CacheConfiguration.EXPIRE_MILLISECONDS);
        }else{
            //有缓存
            System.out.println("缓存命中");
            List<Page<Batch>> pages=(List<Page<Batch>>) cache;
            resultPage=pages.get(currentPageNumber-1);
        }
        return resultPage;
    }
    public Page<Batch> queryByWarehouseId(String wid, int currentPageNumber, int pageSize){
        //前端已校验过wid是表示数字的非空字符串，且为一个正整数
        String cacheKey="batch"+"wid"+wid;
        Object cache= CacheUtils.get(cacheKey);
        Page<Batch> resultPage=null;
        if(cache==null){
            List<Batch> batchesMeetCondition=bm.queryByWarehouseId(Integer.parseInt(wid));
            if(batchesMeetCondition==null||batchesMeetCondition.isEmpty()){
                return null;
            }
            Frame<Batch> frame=new Frame<>(batchesMeetCondition,pageSize);
            List<Page<Batch>> pages=frame.GenerateMeetConditionPages();
            resultPage=pages.get(currentPageNumber-1);
            CacheUtils.put(cacheKey,pages, CacheConfiguration.EXPIRE_MILLISECONDS);
        }else{
            //有缓存
            System.out.println("缓存命中");
            List<Page<Batch>> pages=(List<Page<Batch>>) cache;
            resultPage=pages.get(currentPageNumber-1);
        }
        return resultPage;

    }
    public Page<Batch> queryByQuantityRange(String from, String to, int currentPageNumber, int pageSize){
        //前端已校验过from和to都是表示数字的非空字符串，且为一个整数，另外，保证to严格大于from.
        String cacheKey="batch"+"from"+from+"to"+to;
        Object cache= CacheUtils.get(cacheKey);
        Page<Batch> resultPage=null;
        if(cache==null){
            List<Batch> batchesMeetCondition=bm.queryByQuantityRange(Integer.parseInt(from),Integer.parseInt(to));
            if(batchesMeetCondition==null||batchesMeetCondition.isEmpty()){
                return null;
            }
            Frame<Batch> frame=new Frame<>(batchesMeetCondition,pageSize);
            List<Page<Batch>> pages=frame.GenerateMeetConditionPages();
            resultPage=pages.get(currentPageNumber-1);
            CacheUtils.put(cacheKey,pages, CacheConfiguration.EXPIRE_MILLISECONDS);
        }else{
            //有缓存
            System.out.println("缓存命中");
            List<Page<Batch>> pages=(List<Page<Batch>>) cache;
            resultPage=pages.get(currentPageNumber-1);
        }
        return resultPage;
    }

    public ProcessingResult insert(String cid, String wid, String quantity){
        //前端已校验过wid和cid和quantity都是表示数字的非空字符串，且为一个正整数
        ProcessingResult pr;
        int cid_int=Integer.parseInt(cid);
        int wid_int=Integer.parseInt(wid);
        if(bm.queryByWidAndCid(cid_int,wid_int)!=null){
            return new ProcessingResult(false,"有重复batch");
        }
        int rowsAffected=bm.insert(cid_int,wid_int,Integer.parseInt(quantity));
        if(rowsAffected<1){
            return new ProcessingResult(false,"插入失败");
        }else{
            deleteAllBatchPagesCache();
            return new ProcessingResult(true,"插入成功");
        }
    }

    public ProcessingResult deleteService(String cid, String wid){
        int cid_int=Integer.parseInt(cid);
        int wid_int=Integer.parseInt(wid);
        if(bm.queryByWidAndCid(cid_int,wid_int)==null){
            return new ProcessingResult(false,"目标记录查找不到");
        }
        int rowsAffected=bm.deleteRecord(cid_int,wid_int);
        if(rowsAffected<1){
            return new ProcessingResult(false,"删除失败");
        }else{
            deleteAllBatchPagesCache();
            return new ProcessingResult(true,"删除成功");
        }
        //ok
    }

    @Transactional(rollbackFor=Exception.class,isolation=Isolation.SERIALIZABLE)
    public ProcessingResult inventoryOut(String source_wid,String dest_wid,String cid,String transferQuantity) throws Exception {
        //source_wid,cid来自对元素的文本的摘取，不需要前台校验，一定是表示正整数的串。
        //dest_wid是空串、空、表示正整数的串都是合法，除此三者外皆不合法，前台校验保证这个。
        //前台校验保证Qty是表示正整数的串，但不需要小于等于展示着的batch的现有Qty.
        int source_wid_int=Integer.parseInt(source_wid);
        int cid_int=Integer.parseInt(cid);
        int outQuantity=Integer.parseInt(transferQuantity);
        if(dest_wid==null||dest_wid.equals("")){
            //只是源库的货的单纯减少。
            //数据表设置了quantity大于0的约束

            int rowsAffectedWhileMinus= bm.minusQuantity(cid_int,source_wid_int,outQuantity);
            if(rowsAffectedWhileMinus<1){
                throw new Exception();
            }
            deleteAllBatchPagesCache();
            return new ProcessingResult(true,"成功减少数量");
        }else{
            int dest_wid_int=Integer.parseInt(dest_wid);
            int rowsAffectedWhileMinus=bm.minusQuantity(cid_int,source_wid_int,outQuantity);
            if(rowsAffectedWhileMinus<1){
                throw new Exception();
            }
            if(bm.queryByWidAndCid(cid_int,dest_wid_int)==null){
                int rowsAffectedWhileInsert=bm.insert(cid_int,dest_wid_int,outQuantity);
                if(rowsAffectedWhileInsert<1){
                    throw new Exception();
                }
                deleteAllBatchPagesCache();
                return new ProcessingResult(true,"成功转移至其他库房");
            }
            int rowsAffectedWhilePlus=bm.addQuantity(cid_int,dest_wid_int,outQuantity);
            if(rowsAffectedWhilePlus<1){
                throw new Exception();
            }
            deleteAllBatchPagesCache();
            return new ProcessingResult(true,"成功转移至其他库房");
        }
    }
    @Transactional(rollbackFor=Exception.class,isolation=Isolation.SERIALIZABLE)
    public ProcessingResult inventoryIn(String source_wid,String dest_wid,String cid,String transferQuantity) throws Exception{
        int dest_wid_int=Integer.parseInt(dest_wid);
        int cid_int=Integer.parseInt(cid);
        int outQuantity=Integer.parseInt(transferQuantity);
        if(source_wid==null||source_wid.equals("")){
            //只是目的库的存量的单纯增加。
            int rowsAffectedWhilePlus= bm.addQuantity(cid_int,dest_wid_int,outQuantity);
            if(rowsAffectedWhilePlus<1){
                throw new Exception();
            }
            deleteAllBatchPagesCache();
            return new ProcessingResult(true,"成功增加数量");
        }else{
            int source_wid_int=Integer.parseInt(source_wid);
            int rowsAffectedWhileMinus=bm.minusQuantity(cid_int,source_wid_int,outQuantity);
            if(rowsAffectedWhileMinus<1){
                throw new Exception();
            }
            if(bm.queryByWidAndCid(cid_int,dest_wid_int)==null){
                int rowsAffectedWhileInsert=bm.insert(cid_int,dest_wid_int,outQuantity);
                if(rowsAffectedWhileInsert<1){
                    throw new Exception();
                }
                deleteAllBatchPagesCache();
                return new ProcessingResult(true,"成功转移至目的库房");
            }
            int rowsAffectedWhilePlus=bm.addQuantity(cid_int,dest_wid_int,outQuantity);
            if(rowsAffectedWhilePlus<1){
                throw new Exception();
            }
            deleteAllBatchPagesCache();
            return new ProcessingResult(true,"成功转移至其他库房");
        }

    }
    private void deleteAllBatchPagesCache(){
        Set<String> cacheKeys=CacheUtils.getAllCacheKeys();
        for (String cacheKey : cacheKeys) {
            if(cacheKey.contains("batch")){
                CacheUtils.remove(cacheKey);
            }
        }
    }

}
