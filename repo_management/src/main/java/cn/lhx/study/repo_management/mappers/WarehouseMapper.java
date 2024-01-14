package cn.lhx.study.repo_management.mappers;
import cn.lhx.study.repo_management.entities.Warehouse;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface WarehouseMapper{
    Warehouse selectById(@Param("wid") int wid);
    Warehouse selectByAddr(@Param("addr") String addr);
    List<Warehouse> selectByAddrLike(@Param("like") String like);

    int insert(@Param("addr") String addr);
    int delete(@Param("wid") int wid);
    int modify(@Param("wid") int wid,@Param("addr") String addr);
}
