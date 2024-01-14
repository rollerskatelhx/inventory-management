package cn.lhx.study.repo_management.service_layer;
import cn.lhx.study.repo_management.entities.Sys_user;

public interface InterSysUserService{
    Sys_user loginService(String username,String password,int status);
}
