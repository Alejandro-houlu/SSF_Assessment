package sg.edu.nus.iss.SSF_Assessment.Controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.SSF_Assessment.Models.Quotation;
import sg.edu.nus.iss.SSF_Assessment.Services.QuotationService;

@RestController
@RequestMapping(path="/api/po", produces = MediaType.APPLICATION_JSON_VALUE)
public class PurchaseOrderRestController {

    @Autowired
    QuotationService qService;

    @PostMapping
    public ResponseEntity<String> result(@RequestBody String body){
        System.out.println(">>>>>>>>>>>>>>>>>>>BODY: " + body);

        List<String> items = qService.getItems(body);
        Map<String, Integer> order = qService.getOrder(body);
        items.stream().forEach(System.out::println);
        Optional<Quotation> opQuote = qService.getQuotations(items);

        if(opQuote.isEmpty()){
            return ResponseEntity.status(404).body(Json.createObjectBuilder().build().toString());
        }


        Quotation quote = opQuote.get();
        Float price = qService.getPrice(quote, order);

        JsonObject obj = Json.createObjectBuilder().add("invoiceId", quote.getQuoteId())
                .add("name", qService.getName(body))
                .add("total", price).build();

        return ResponseEntity.ok(obj.toString());
    }
    
}
