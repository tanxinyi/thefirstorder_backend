package MakaNow.thefirstorder_back.controller;


import MakaNow.thefirstorder_back.model.OrderSummary;
import MakaNow.thefirstorder_back.model.Orders;
import MakaNow.thefirstorder_back.model.SeatingTable;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.OrdersRepository;
import MakaNow.thefirstorder_back.repository.SeatingTableRepository;
import MakaNow.thefirstorder_back.service.OrdersService;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrdersController {

    private final String PREFIX = "O";
    private final String FIRST_ID = "O000";

    Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private SeatingTableController seatingTableController;

    @Autowired
    private OrderSummaryController orderSummaryController;

    @GetMapping("/orders")
    @JsonView(View.OrdersView.class)
    public Iterable<Orders> getAllOrders(){
        return ordersService.list();
    }

    protected String getLatestOID(){
        Iterable<Orders> orderSummaries = ordersRepository.findAll();

        String latestOID = FIRST_ID;
        for( Orders orderSummary : orderSummaries){
            String currentOID = orderSummary.getOrderId();
            if(latestOID.equals("") || currentOID.compareTo(latestOID) > 0){
                latestOID = currentOID;
            }
        }
        return latestOID;
    }

    @GetMapping("/orders/{orderId}")
    @JsonView(View.OrdersView.class)
    public Orders getOrdersById(@PathVariable String orderId) throws NotFoundException{
        Optional<Orders> optionalOrders = ordersRepository.findById(orderId);
        if(optionalOrders.isPresent()){
            return optionalOrders.get();
        }else{
            throw new NotFoundException("Order ID " + orderId + " does not exist");
        }
    }

    @GetMapping("/orders/new/orderSummary/{orderSummaryId}")
    @JsonView(View.OrdersView.class)
    public Orders getNewOrders(@PathVariable String orderSummaryId) throws NotFoundException{
        logger.info("OrderSumaryId:" + orderSummaryId);
        String latestOID = getLatestOID();
        String newCount = "" + (Integer.parseInt(latestOID.substring(PREFIX.length())) + 1);
        latestOID = PREFIX;
        for(int i=0; i<(FIRST_ID.length()-PREFIX.length()-newCount.length()); i++){
            latestOID += "0";
        }
        latestOID += newCount;
        OrderSummary orderSummary = orderSummaryController.getOrderSummaryById(orderSummaryId);
        Orders order = new Orders(latestOID, orderSummary, 0.0);
        return ordersRepository.save(order);
    }
}

