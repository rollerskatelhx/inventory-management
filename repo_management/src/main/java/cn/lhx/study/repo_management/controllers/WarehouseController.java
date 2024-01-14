package cn.lhx.study.repo_management.controllers;
import cn.lhx.study.repo_management.entities.Page;
import cn.lhx.study.repo_management.entities.ProcessingResult;
import cn.lhx.study.repo_management.entities.Warehouse;
import cn.lhx.study.repo_management.service_layer.InterWarehouseService;
import cn.lhx.study.repo_management.wheel.JsonStringGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {
    @Autowired
    private InterWarehouseService warehouseService;

    @RequestMapping("/select_by_keyword")
    @ResponseBody
    public String selectByAddrKeyword(@RequestParam String keyword, @RequestParam String currentPageNumber, @RequestParam String pageSize){
        Page<Warehouse> page=warehouseService.selectByAddrKeyword(keyword,Integer.parseInt(currentPageNumber),Integer.parseInt(pageSize));
        String json="";
        try {
            if(page==null){
                ProcessingResult pr=new ProcessingResult(false,"没有找到相应记录");
                json= JsonStringGenerator.om.writeValueAsString(pr);
            }else{
                json=JsonStringGenerator.om.writeValueAsString(page);
            }
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException");
        }
        return json;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String deleteRecord(@RequestParam String wid){
        ProcessingResult pr=warehouseService.deleteService(wid);
        return JsonStringGenerator.generate(pr);
    }

    @RequestMapping("/selectById")
    @ResponseBody
    public String selectById(@RequestParam String wid){
        Warehouse warehouse=warehouseService.selectById(wid);
        String json="";
        try {
            if(warehouse==null){
                ProcessingResult pr=new ProcessingResult(false,"没有找到相应记录");
                json=JsonStringGenerator.om.writeValueAsString(pr);
            }else{
                Page<Warehouse> page=new Page<>();
                page.pageQuantity=1;
                page.pageNum=1;
                page.itemsOfPage=new ArrayList<>();
                page.itemsOfPage.add(warehouse);
                json=JsonStringGenerator.om.writeValueAsString(page);
            }
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException");
        }
        return json;
    }

    @RequestMapping("/modify")
    @ResponseBody
    public String modifyRecord(@RequestParam String wid,@RequestParam String addr){
        ProcessingResult pr=warehouseService.modifyService(wid,addr);
        return JsonStringGenerator.generate(pr);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public String insert(@RequestParam String addr){
        ProcessingResult pr=warehouseService.insertService(addr);
        return JsonStringGenerator.generate(pr);
    }
}
