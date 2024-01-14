package cn.lhx.study.repo_management;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.lhx.study.repo_management.mappers")
public class RepoManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(RepoManagementApplication.class, args);
    }

}
