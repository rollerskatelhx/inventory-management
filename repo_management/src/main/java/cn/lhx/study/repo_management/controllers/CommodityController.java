package cn.lhx.study.repo_management.controllers;
import cn.lhx.study.repo_management.entities.ProcessingResult;
import cn.lhx.study.repo_management.wheel.JsonStringGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.lhx.study.repo_management.entities.Commodity;
import cn.lhx.study.repo_management.entities.Page;
import cn.lhx.study.repo_management.service_layer.InterCommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;

@Controller
@RequestMapping("/commodity")
//读写用户共享
public class CommodityController{
    @Autowired
    private InterCommodityService cs;

    @RequestMapping("/select_by_keyword")
    @ResponseBody
    public String selectByKeyword(@RequestParam String keyword,@RequestParam String currentPageNumber,@RequestParam String pageSize){
        Page<Commodity> page=cs.selectByKeyword(keyword,Integer.parseInt(currentPageNumber),Integer.parseInt(pageSize));
        String json="";
        try {
            if(page==null){
                ProcessingResult pr=new ProcessingResult(false,"没有找到相应记录");
                json=JsonStringGenerator.om.writeValueAsString(pr);
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
    public String deleteRecord(@RequestParam String cid){
        ProcessingResult pr=cs.delete(cid);
        return JsonStringGenerator.generate(pr);
    }

    @RequestMapping("/select_by_cid")
    @ResponseBody
    public String selectById(@RequestParam String cid){
        Commodity commodity=cs.selectById(Integer.parseInt(cid));

        String json="";
        try {
            if(commodity==null){
                ProcessingResult pr=new ProcessingResult(false,"没有找到相应记录");
                json=JsonStringGenerator.om.writeValueAsString(pr);
            }else{
                Page<Commodity> page=new Page<>();
                page.pageQuantity=1;
                page.pageNum=1;
                page.itemsOfPage=new ArrayList<>();
                page.itemsOfPage.add(commodity);
                json=JsonStringGenerator.om.writeValueAsString(page);
            }
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException");
        }
        return json;
    }

    @RequestMapping("/modify")
    @ResponseBody
    public String modifyCommodityRecord(@RequestParam String cid,@RequestParam String name,@RequestParam String unit_price){
        ProcessingResult pr=cs.modifyService(cid,name,unit_price);
        return JsonStringGenerator.generate(pr);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public String insertCommodity(@RequestParam String name,@RequestParam String unit_price){
        ProcessingResult pr=cs.insertService(name,unit_price);
        return JsonStringGenerator.generate(pr);
    }
}
