package cn.lhx.study.repo_management.interceptors;
import cn.lhx.study.repo_management.entities.Sys_user;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Order(1)
public class CheckLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        HttpSession session= request.getSession();
        Sys_user u=(Sys_user) session.getAttribute("user");
        if(u==null){
            //没有登录成功
            //此方法返回false表示直接忽略当前请求
            System.out.println("没登录，已被拦截");
            return false;
        }else{
            return true;
        }
    }
}
