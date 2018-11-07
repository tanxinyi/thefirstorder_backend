package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Customer;
import MakaNow.thefirstorder_back.model.OrderSummary;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.OrderSummaryRepository;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class OrderSummaryController {

    private final String PREFIX = "OS";
    private final String FIRST_ID = "OS001";

    @Autowired
    private OrderSummaryRepository orderSummaryRepository;

    @Autowired
    private CustomerController customerController;


    private String getLatestOSID(){
        Iterable<OrderSummary> orderSummaries = orderSummaryRepository.findAll();

        String latestOSID = FIRST_ID;
        for( OrderSummary orderSummary : orderSummaries){
            String currentOSID = orderSummary.getOrderSummaryId();
            if(latestOSID.equals("") || currentOSID.compareTo(latestOSID) > 0){
                latestOSID = currentOSID;
            }
        }
        return latestOSID;
    }

    @GetMapping("/orderSummary/new/customer/{customerId}")
    @JsonView(View.ViewB.class)
    public OrderSummary getNewOrderSummary(@PathVariable String customerId) throws NotFoundException {
        String latestOSID = getLatestOSID();
        String newCount = "" + (Integer.parseInt(latestOSID.substring(PREFIX.length())) + 1);
        latestOSID = PREFIX;
        for(int i=0; i<(FIRST_ID.length()-PREFIX.length()-newCount.length()); i++){
            latestOSID += "0";
        }
        latestOSID += newCount;
        Customer customer = customerController.getCustomerById(customerId);
        OrderSummary orderSummary = new OrderSummary(latestOSID, customer,"Pending", 0.0, new Date(), "Cash");
        return orderSummaryRepository.save(orderSummary);
    }

    @GetMapping("/orderSummary/{orderSummaryId}")
    @JsonView(View.ViewB.class)
    public OrderSummary getOrderSummaryById(@PathVariable String orderSummaryId) throws NotFoundException{
        Optional<OrderSummary> optionalOrderSummary = orderSummaryRepository.findById(orderSummaryId);
        if(optionalOrderSummary.isPresent()){
            return optionalOrderSummary.get();
        }else{
            throw new NotFoundException("Order Summary ID " + orderSummaryId + " does not exist");
        }
    }
}
