package cn.lhx.study.repo_management.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Batch{
    private int cid;
    private int wid;
    private int quantity;
}
