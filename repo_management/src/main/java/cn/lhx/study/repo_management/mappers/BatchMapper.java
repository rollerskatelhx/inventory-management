package cn.lhx.study.repo_management.mappers;
import cn.lhx.study.repo_management.entities.Batch;
import org.apache.ibatis.annotations.Param;
import java.util.List;


public interface BatchMapper{
    List<Batch> selectByCommodityId(@Param("cid") int cid);
    List<Batch> queryByWarehouseId(@Param("wid") int wid);
    List<Batch> queryByQuantityRange(@Param("from") int from,@Param("to") int to);
    int deleteRecord(@Param("cid") int cid,@Param("wid") int wid);
    int insert(@Param("cid") int cid,@Param("wid") int wid,@Param("quantity") int quantity);
    int addQuantity(@Param("cid") int cid,@Param("wid") int wid,@Param("quantity") int quantity);
    Batch queryByWidAndCid(@Param("cid") int cid,@Param("wid") int wid);

    int minusQuantity(@Param("cid") int cid,@Param("wid") int wid,@Param("quantity") int quantity);
}
