package cn.lhx.study.repo_management.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Log{
    private int lid;
    private int uid;
    private String invoke_description;
    private LocalDateTime start_time;
    private int time_cost;
    private boolean success;
}
