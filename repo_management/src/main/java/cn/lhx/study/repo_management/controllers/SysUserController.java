package cn.lhx.study.repo_management.controllers;
import cn.lhx.study.repo_management.entities.Sys_user;
import cn.lhx.study.repo_management.service_layer.InterSysUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SysUserController{
    @Autowired
    private InterSysUserService sysUserService;

    @PostMapping("/login_handler")
    public String loginHandle(@RequestParam String username, @RequestParam String password, @RequestParam String status, HttpServletRequest request, Model model){
        int status_int=Integer.parseInt(status);
        Sys_user loginResult=sysUserService.loginService(username,password,status_int);
        if(loginResult==null){
            request.getSession().setAttribute("login_flag",-1);
            return "login";
        }else{
            //登录成功
            request.getSession().setAttribute("user",loginResult);
            request.getSession().setAttribute("login_flag",1);
            return "home";
        }
    }
}
