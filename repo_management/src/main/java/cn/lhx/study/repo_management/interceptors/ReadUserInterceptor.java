package cn.lhx.study.repo_management.interceptors;
import cn.lhx.study.repo_management.entities.Sys_user;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Order(2)
public class ReadUserInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        HttpSession session= request.getSession();
        Sys_user u=(Sys_user) session.getAttribute("user");
        if(u.getStatus()==1){
            //1是读用户
            //此方法返回false表示直接忽略当前请求
            System.out.println("非写用户访问，已被拦截");
            return false;
        }else{
            return true;
        }
    }
}
