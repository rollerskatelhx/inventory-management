package cn.lhx.study.repo_management;
import cn.lhx.study.repo_management.interceptors.CheckLoginInterceptor;
import cn.lhx.study.repo_management.interceptors.ReadUserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RepoManagementMvcConfigurer implements WebMvcConfigurer{
    @Autowired
    private CheckLoginInterceptor cli;

    @Autowired
    private ReadUserInterceptor rui;

    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(cli).addPathPatterns("/**").excludePathPatterns("/","/login_handler");//correct.

        registry.addInterceptor(rui).addPathPatterns("/commodity/delete","/commodity/modify","/commodity/insert","/batch/insert","/batch/delete","/batch/inventoryOut","/batch/inventoryIn","/warehouse/delete","/warehouse/insert","/warehouse/modify");
    }
}
