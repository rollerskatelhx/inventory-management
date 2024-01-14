package cn.lhx.study.repo_management.mappers;
import cn.lhx.study.repo_management.entities.Sys_user;
import org.apache.ibatis.annotations.Param;

public interface SysUserMapper{
    Sys_user login(@Param("username") String username,@Param("status") int status,@Param("password") String password);
}
