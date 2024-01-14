package cn.lhx.study.repo_management.wheel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonStringGenerator {
    public static final ObjectMapper om=new ObjectMapper();
    public static String generate(Object obj){
        String json="";
        try {
            json=om.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            System.out.println("JsonProcessingException");
        }
        return json;
    }
}
