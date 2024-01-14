package cn.lhx.study.repo_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Fork {
    @RequestMapping("/commodity_management")
    public String reachCommodityManagement(){
        return "commodity_management";
    }
    @RequestMapping("/warehouse_management")
    public String reachWarehouseManagement(){
        return "warehouse_management";
    }
    @RequestMapping("/inventory")
    public String reachInventory(){
        return "inventory";
    }
}
