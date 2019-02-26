package MakaNow.thefirstorder_back.controller;


import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.OrdersRepository;
import MakaNow.thefirstorder_back.repository.SeatingTableRepository;
import MakaNow.thefirstorder_back.service.OrdersService;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
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

    @Autowired
    private RestaurantController restaurantController;

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
        logger.info("OrderSummaryId:" + orderSummaryId);
        String latestOID = getLatestOID();
        String newCount = "" + (Integer.parseInt(latestOID.substring(PREFIX.length())) + 1);
        latestOID = PREFIX;
        for(int i=0; i<(FIRST_ID.length()-PREFIX.length()-newCount.length()); i++){
            latestOID += "0";
        }
        latestOID += newCount;
        OrderSummary orderSummary = orderSummaryController.getOrderSummaryById(orderSummaryId);
        Orders order = new Orders(latestOID, orderSummary, 0.0, "PENDING");
        return ordersRepository.save(order);
    }

//    @GetMapping("/orders/restaurant/{restaurantId}/retrieve_new_orders/")
//    @JsonView(View.OrdersView.class)
//    public List<Orders> retrieveNewOrders(@PathVariable String restaurantId) throws NotFoundException{
//        logger.info("Retrieving unsent orders by restaurant: " + restaurantId);
//        List<Orders> orders = (List) ordersRepository.findAll();
//        List<Orders> output = new ArrayList<>();
//        for(Orders order: orders){
//            if(order.getOrderStatus().equals("PENDING") && order.getCustomerOrders().size() > 0 ) {
//                Restaurant restaurant = order.getOrderSummary().getSeatingTable().getRestaurant();
//                if(restaurant.getRestaurantId().equals(restaurantId)){
//                    order.setOrderStatus("SENT");
//                    output.add(order);
//                }
//            }
//        }
//
//        return (List) ordersRepository.saveAll((Iterable<Orders>)output);
//    }

    @GetMapping("/orders/restaurant/{restaurantId}/retrieve_sent_orders/")
    @JsonView(View.OrdersView.class)
    public List<Orders> retrieveSentOrders(@PathVariable String restaurantId) throws NotFoundException{
        logger.info("Retrieving sent orders by restaurant: " + restaurantId);
        List<Orders> orders = (List) ordersRepository.findAll();
        List<Orders> output = new ArrayList<>();
        for(Orders order: orders){
            if(order.getOrderStatus().equals("SENT") && order.getCustomerOrders().size() > 0 ) {
                Restaurant restaurant = order.getOrderSummary().getSeatingTable().getRestaurant();
                if(restaurant.getRestaurantId().equals(restaurantId)){
                    output.add(order);
                }
            }
        }
        return (List) ordersRepository.saveAll((Iterable<Orders>)output);
    }

    @PostMapping("/orders/acknowledge/{restaurantId}/{orderId}/")
    public ResponseEntity<?> acknowledgeOrder( @PathVariable("restaurantId") String restaurantId, @PathVariable("orderId") String orderId) {
        Optional<Orders> optionalOrder = ordersRepository.findById(orderId);
        Orders orders = optionalOrder.get();
        orders.setOrderStatus("ACKNOWLEDGED");
        ordersRepository.save(orders);
        return new ResponseEntity("Order Acknowledged", HttpStatus.OK);
    }

    @GetMapping("/orders/restaurant/{restaurantId}/retrieve_acknowledged_orders/")
    @JsonView(View.OrdersView.class)
    public List<Orders> retrieveAcknowledgedOrders(@PathVariable String restaurantId) throws NotFoundException{
        logger.info("Retrieving acknowledged orders by restaurant: " + restaurantId);
        List<Orders> orders = (List) ordersRepository.findAll();
        List<Orders> output = new ArrayList<>();
        for(Orders order: orders){
            if(order.getOrderStatus().equals("ACKNOWLEDGED") && order.getOrderSummary().getPaymentStatus().equals("Pending") ) {
                Restaurant restaurant = order.getOrderSummary().getSeatingTable().getRestaurant();
                if(restaurant.getRestaurantId().equals(restaurantId)){
                    output.add(order);
                }
            }
        }
        Collections.reverse(output);
        return (List) ordersRepository.saveAll((Iterable<Orders>)output);
    }
}

