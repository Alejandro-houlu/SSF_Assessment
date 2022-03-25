package sg.edu.nus.iss.SSF_Assessment.Repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;


import org.springframework.stereotype.Repository;

@Repository
public class QuotationRepository {

    public Map<String,Integer> getItems(String body){
        // String query = "$['lineItems']";
         DocumentContext jsonContext = JsonPath.parse(body);
        // List<Map<String, String>> itemMap = jsonContext.read(query);
        // List<String> items = itemMap.stream().map(m->m.get("item")).toList();

        List<String> items = jsonContext.read("$.*.*.item");
        List<Integer> quantity = jsonContext.read("$.*.*quantity");
        Map<String,Integer> requestMap = new HashMap<>();
        for(int i = 0; i < items.size(); i++){
            requestMap.put(items.get(i), quantity.get(i));
        }

        return requestMap; 
        
    }

    public String getName(String body){
        DocumentContext jsonContext = JsonPath.parse(body);
        String name = jsonContext.read("$.name");

        return name;
    }

    public String getId(String resp){
        DocumentContext jsonContext = JsonPath.parse(resp);
        String id = jsonContext.read("$.quoteId");

        return id;
    }

    public Map<String, Float> quotes(String resp){

        DocumentContext jsonContext = JsonPath.parse(resp);
        List<String> key = jsonContext.read("$.quotations.*.item");
        List<Number> value = jsonContext.read("$.quotations.*.unitPrice");
        Map<String, Float> quoteMap = new HashMap<>();

        for(int i = 0; i < key.size(); i++){
        quoteMap.put(key.get(i),value.get(i).floatValue());
        }

        return quoteMap;
    }
    
}
