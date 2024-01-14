package cn.lhx.study.repo_management.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Beginning {
    @RequestMapping("/")
    public String begin(){
        return "login";
    }
}
