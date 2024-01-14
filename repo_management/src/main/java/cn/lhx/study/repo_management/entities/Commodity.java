package cn.lhx.study.repo_management.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Commodity {
    private int cid;
    private String name;//尽管非主键，但建表时设置了unique.
    private int unit_price;
}
