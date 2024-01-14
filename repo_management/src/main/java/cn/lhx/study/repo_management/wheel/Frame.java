package cn.lhx.study.repo_management.wheel;
import cn.lhx.study.repo_management.entities.Page;
import java.util.ArrayList;
import java.util.List;

public class Frame<T>{
    private List<T> allEntitiesMeetCondition;
    private int pageSize;

    public Frame(List<T> allEntitiesMeetCondition,int pageSize){
        this.allEntitiesMeetCondition=allEntitiesMeetCondition;
        this.pageSize=pageSize;
    }
    public List<Page<T>> GenerateMeetConditionPages(){
        int totalQty=allEntitiesMeetCondition.size();
        int pageQty=totalQty/pageSize;
        int yu=totalQty%pageSize;
        if(yu>0){
            pageQty=pageQty+1;
        }
        List<Page<T>> pages=new ArrayList<>();
        for(int pageNum=1;pageNum<=pageQty;pageNum++){
            int offset=pageSize*(pageNum-1);
            int limit=0;
            int shang=totalQty/pageSize;
            if(pageNum<=shang){
                limit=pageSize;
            }else{
                limit=totalQty%pageSize;
            }
            Page<T> p=new Page<>();
            p.pageNum=pageNum;
            p.pageQuantity=pageQty;
            p.itemsOfPage=allEntitiesMeetCondition.subList(offset,offset+limit);
            pages.add(p);
        }
        return pages;
    }
}
