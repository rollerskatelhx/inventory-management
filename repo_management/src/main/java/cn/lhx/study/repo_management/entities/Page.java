package cn.lhx.study.repo_management.entities;
import java.util.List;

public class Page<T>{
    public int pageNum;
    public List<T> itemsOfPage;
    public int pageQuantity;
}
