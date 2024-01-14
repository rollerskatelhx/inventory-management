package cn.lhx.study.repo_management.controllers;

import cn.lhx.study.repo_management.entities.Batch;
import cn.lhx.study.repo_management.entities.Page;
import cn.lhx.study.repo_management.entities.ProcessingResult;
import cn.lhx.study.repo_management.service_layer.InterBatchService;
import cn.lhx.study.repo_management.wheel.JsonStringGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/batch")
public class BatchController {
    @Autowired
    private InterBatchService batchService;

    @RequestMapping("/queryByCid")
    @ResponseBody
    public String queryByCommodityId(@RequestParam String cid,@RequestParam String currentPageNumber,@RequestParam String pageSize){
        Page<Batch> batchPage=batchService.queryByCommodityId(cid,Integer.parseInt(currentPageNumber),Integer.parseInt(pageSize));
        String json="";
        try {
            if(batchPage==null){
                ProcessingResult pr=new ProcessingResult(false,"没有找到相应记录");
                json= JsonStringGenerator.om.writeValueAsString(pr);
            }else{
                json=JsonStringGenerator.om.writeValueAsString(batchPage);
            }
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException");
        }
        return json;
    }

    @RequestMapping("/queryByWid")
    @ResponseBody
    public String queryByWarehouseId(@RequestParam String wid,@RequestParam String currentPageNumber,@RequestParam String pageSize){
        Page<Batch> batchPage=batchService.queryByWarehouseId(wid,Integer.parseInt(currentPageNumber),Integer.parseInt(pageSize));
        String json="";
        try {
            if(batchPage==null){
                ProcessingResult pr=new ProcessingResult(false,"没有找到相应记录");
                json= JsonStringGenerator.om.writeValueAsString(pr);
            }else{
                json=JsonStringGenerator.om.writeValueAsString(batchPage);
            }
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException");
        }
        return json;
    }

    @RequestMapping("/queryByQuantityRange")
    @ResponseBody
    public String queryByQuantityRange(@RequestParam String from,@RequestParam String to,@RequestParam String currentPageNumber,@RequestParam String pageSize){
        Page<Batch> batchPage=batchService.queryByQuantityRange(from,to,Integer.parseInt(currentPageNumber),Integer.parseInt(pageSize));
        String json="";
        try {
            if(batchPage==null){
                ProcessingResult pr=new ProcessingResult(false,"没有找到相应记录");
                json= JsonStringGenerator.om.writeValueAsString(pr);
            }else{
                json=JsonStringGenerator.om.writeValueAsString(batchPage);
            }
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException");
        }
        return json;
    }

    @RequestMapping("/insert")
    @ResponseBody
    public String insert(@RequestParam String cid,@RequestParam String wid,@RequestParam String quantity){
        ProcessingResult pr=batchService.insert(cid,wid,quantity);
        return JsonStringGenerator.generate(pr);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam String cid,@RequestParam String wid){
        ProcessingResult pr=batchService.deleteService(cid,wid);
        return JsonStringGenerator.generate(pr);
    }

    @RequestMapping("/inventoryOut")
    @ResponseBody
    public String inventoryOut(@RequestParam String source_wid,@RequestParam String dest_wid,@RequestParam String cid,@RequestParam String transferQuantity){
        ProcessingResult pr;
        try {
            pr=batchService.inventoryOut(source_wid,dest_wid,cid,transferQuantity);
        } catch (Exception e) {
            pr=new ProcessingResult(false,"inventoryOut失败");
        }
        return JsonStringGenerator.generate(pr);
    }

    @RequestMapping("/inventoryIn")
    @ResponseBody
    public String inventoryIn(@RequestParam String source_wid,@RequestParam String dest_wid,@RequestParam String cid,@RequestParam String transferQuantity){
        ProcessingResult pr;
        try {
            pr=batchService.inventoryIn(source_wid,dest_wid,cid,transferQuantity);
        } catch (Exception e) {
            pr=new ProcessingResult(false,"inventoryIn失败");
        }
        return JsonStringGenerator.generate(pr);
    }
}
