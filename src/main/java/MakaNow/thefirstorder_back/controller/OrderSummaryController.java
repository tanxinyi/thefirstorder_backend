package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Customer;
import MakaNow.thefirstorder_back.model.OrderSummary;
import MakaNow.thefirstorder_back.model.SeatingTable;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.OrderSummaryRepository;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class OrderSummaryController {

    private final String PREFIX = "OS";
    private final String FIRST_ID = "OS000";

    Logger logger = LoggerFactory.getLogger(OrderSummaryController.class);

    @Autowired
    private OrderSummaryRepository orderSummaryRepository;

    @Autowired
    private CustomerController customerController;

    @Autowired
    private SeatingTableController seatingTableController;

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

    @GetMapping("/orderSummary/new/customer/{customerId}/seatingTable/{qrCode}")
    @JsonView(View.OrderSummaryView.class)
    public OrderSummary getNewOrderSummary(@PathVariable String customerId, @PathVariable String qrCode) throws NotFoundException {
        logger.info("SeatingTableID:" + qrCode);
        String latestOSID = getLatestOSID();
        String newCount = "" + (Integer.parseInt(latestOSID.substring(PREFIX.length())) + 1);
        latestOSID = PREFIX;
        for(int i=0; i<(FIRST_ID.length()-PREFIX.length()-newCount.length()); i++){
            latestOSID += "0";
        }
        latestOSID += newCount;
        Customer customer = customerController.getCustomerById(customerId);
        SeatingTable seatingTable = seatingTableController.getSeatingTableBySeatingTableId(qrCode);
        OrderSummary orderSummary = new OrderSummary(latestOSID, customer,"Pending", 0.0, new Date(), "Card", seatingTable);
        return orderSummaryRepository.save(orderSummary);
    }

    @PutMapping("/orderSummary/{orderSummaryId}/paymentStatus/{paymentStatus}")
    @JsonView(View.OrderSummaryView.class)
    public OrderSummary updateOrderSummaryPaymentStatus(@PathVariable String orderSummaryId,
                                                        @PathVariable String paymentStatus) throws NotFoundException {
        logger.info("Update Payment Status");
        logger.info("OrderSummaryId: " + orderSummaryId);
        OrderSummary orderSummary = getOrderSummaryById(orderSummaryId);
        orderSummary.setPaymentStatus(paymentStatus);
        return orderSummaryRepository.save(orderSummary);
    }

    @GetMapping("/orderSummary/{orderSummaryId}")
    @JsonView(View.OrderSummaryView.class)
    public OrderSummary getOrderSummaryById(@PathVariable String orderSummaryId) throws NotFoundException{
        Optional<OrderSummary> optionalOrderSummary = orderSummaryRepository.findById(orderSummaryId);
        if(optionalOrderSummary.isPresent()){
            return optionalOrderSummary.get();
        }else{
            throw new NotFoundException("Order Summary ID " + orderSummaryId + " does not exist");
        }
    }
}
