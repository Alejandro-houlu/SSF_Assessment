package sg.edu.nus.iss.SSF_Assessment.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import com.jayway.jsonpath.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import sg.edu.nus.iss.SSF_Assessment.Models.Quotation;
import sg.edu.nus.iss.SSF_Assessment.Repo.QuotationRepository;

@Service
public class QuotationService {

    private String url = "https://quotation.chuklee.com/quotation";

    @Autowired
    QuotationRepository qRepo;

    public List<String> getItems(String body){

        Map<String, Integer> temp = qRepo.getItems(body);
        Set<String>keys = temp.keySet();
        List<String> items = new ArrayList<>();

        for(String k : keys){
            items.add(k);
        }    
        
        return items;
    }

    public Map<String, Integer> getOrder(String body){
        return qRepo.getItems(body);
    }

    public String getName(String body){
        return qRepo.getName(body);
    }


    public Optional<Quotation> getQuotations(List<String> items){

        String jsonItemList = JSONArray.toJSONString(items);

        System.out.println(jsonItemList);

        String id = "";
        Map<String, Float> quotes = new HashMap<>();

        try{
        RequestEntity<String> req = RequestEntity.post(url).contentType(MediaType.APPLICATION_JSON).body(jsonItemList, String.class);
        RestTemplate rTemplate = new RestTemplate();
        ResponseEntity<String> resp = rTemplate.exchange(req, String.class);
        id = qRepo.getId(resp.getBody());
        quotes = qRepo.quotes(resp.getBody());
        }catch(Exception ex){
            return Optional.empty();
        }

        Quotation quote = new Quotation();
        quote.setQuoteId(id);
        quote.setQuotations(quotes);

        return Optional.of(quote);

    }

    public Float getPrice(Quotation quote, Map<String,Integer> order){

        Map<String, Float> quotations = quote.getQuotations();
        Set<String> keys = order.keySet();
        Float price = 0f;

        for(String k : keys){  
            Float q = quotations.get(k);
            Integer o = order.get(k);
            Float price1 = q * o;      
            price += price1;
        }

        return price;

    }
    
}
