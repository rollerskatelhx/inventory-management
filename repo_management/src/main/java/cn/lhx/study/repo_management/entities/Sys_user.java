package cn.lhx.study.repo_management.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sys_user{
    private int id;
    private String username;
    private int status;
    private String password;
}
