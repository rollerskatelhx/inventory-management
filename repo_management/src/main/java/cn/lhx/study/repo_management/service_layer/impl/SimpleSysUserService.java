package cn.lhx.study.repo_management.service_layer.impl;
import cn.lhx.study.repo_management.entities.Sys_user;
import cn.lhx.study.repo_management.mappers.SysUserMapper;
import cn.lhx.study.repo_management.service_layer.InterSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleSysUserService implements InterSysUserService{
    @Autowired
    private SysUserMapper suMapper;

    @Override
    public Sys_user loginService(String username,String password,int status) {
        return suMapper.login(username,status,password);
    }
}
