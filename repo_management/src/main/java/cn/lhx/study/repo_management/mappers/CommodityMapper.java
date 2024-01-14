package cn.lhx.study.repo_management.mappers;
import cn.lhx.study.repo_management.entities.Commodity;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CommodityMapper{
    Commodity selectById(@Param("cid") int cid);
    List<Commodity> selectByNameLike(@Param("like") String like);
    void delete(@Param("cid") int cid);

    int modify(@Param("cid") int cid,@Param("name") String name,@Param("unit_price") int unit_price);
    Commodity selectByName(@Param("name") String name);

    void insert(@Param("name") String name,@Param("unit_price") int unit_price);
}
