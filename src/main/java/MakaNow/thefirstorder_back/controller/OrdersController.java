package MakaNow.thefirstorder_back.controller;


import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.AdminRepository;
import MakaNow.thefirstorder_back.repository.CustomerRepository;
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
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantController restaurantController;

    @Autowired
    private CustomerController customerController;

    @Autowired
    private AdminRepository adminRepository;

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

    @GetMapping("/orders/new/customer/{email}/seatingTable/{qrCode}")
    @JsonView(View.OrdersView.class)
    public Orders getNewOrders(@PathVariable String email,
                               @PathVariable String qrCode) throws NotFoundException{
        logger.info("Getting New Orders");
        logger.info("Customer:" + email);
        logger.info("SeatingTableID:" + qrCode);
        String latestOID = getLatestOID();
        String newCount = "" + (Integer.parseInt(latestOID.substring(PREFIX.length())) + 1);
        latestOID = PREFIX;
        for(int i=0; i<(FIRST_ID.length()-PREFIX.length()-newCount.length()); i++){
            latestOID += "0";
        }
        latestOID += newCount;
        Customer customer = customerController.getCustomerById(email);
        SeatingTable seatingTable = seatingTableController.getSeatingTableBySeatingTableId(qrCode);
        Orders order = new Orders(latestOID,
                0.0,
                "PENDING",
                "PENDING",
                "CARD",
                customer,
                seatingTable);
        return ordersRepository.save(order);
    }

    @PutMapping("/orders/{orderId}/payment/status")
    @JsonView(View.OrderSummaryView.class)
    public Orders updateOrderPaymentStatus(@PathVariable String orderId,
                                                        @RequestParam("status") String paymentStatus
                                           ) throws NotFoundException {
        logger.info("Update Payment Status");
        logger.info("OrderId: " + orderId);
        Orders order = getOrdersById(orderId);
        order.setPaymentStatus(paymentStatus);

        logger.info("Updating Customer Loyalty Points");
        Customer customer = order.getCustomer();
        int currentLoyaltyPoints = customer.getLoyaltyPoint();
        logger.info("Old: " + currentLoyaltyPoints);
        double pointsEarned = order.getTotalAmount() * ordersRepository.findById(orderId).get().getSeatingTable().getRestaurant().getMoneyToPointsConversionRate();
        customer.setLoyaltyPoint(currentLoyaltyPoints + (int)pointsEarned);
        logger.info("New: " + customer.getLoyaltyPoint());
        customerRepository.save(customer);
        logger.info("Customer loyalty point updated");

        return ordersRepository.save(order);
    }

    @PutMapping("/orders/{orderId}/payment/token")
    @JsonView(View.OrderSummaryView.class)
    public Orders updateTokenAndAmount(@PathVariable String orderId,
                                           @RequestParam("amount") int amount,
                                           @RequestParam("token") String token
    ) throws NotFoundException {
        logger.info("Insert Token");
        logger.info("OrderId: " + orderId);
        Orders order = getOrdersById(orderId);
        logger.info("Updating Amount: " + amount/100.0);
        order.setTotalAmount(amount/100.0);
        logger.info("Updating Status");
        order.setPaymentStatus("READY");
        logger.info("Updating Token");
        order.setToken(token);

        return ordersRepository.save(order);
    }

    @GetMapping("/orders/restaurant/{restaurantId}/retrieve_sent_orders/")
    @JsonView(View.OrdersView.class)
    public List<Orders> retrieveSentOrders(@PathVariable String restaurantId) throws NotFoundException{
        logger.info("Retrieving sent orders by restaurant: " + restaurantId);
        List<Orders> orders = (List) ordersRepository.findAll();
        List<Orders> output = new ArrayList<>();
        for(Orders order: orders){
            if(order.getOrderStatus().equals("SENT") && order.getCustomerOrders().size() > 0 ) {
                Restaurant restaurant = order.getSeatingTable().getRestaurant();
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
            if(order.getOrderStatus().equals("ACKNOWLEDGED") && order.getPaymentStatus().equals("Pending") ) {
                Restaurant restaurant = order.getSeatingTable().getRestaurant();
                if(restaurant.getRestaurantId().equals(restaurantId)){
                    output.add(order);
                }
            }
        }
        Collections.reverse(output);
        return (List) ordersRepository.saveAll((Iterable<Orders>)output);
    }

    @PostMapping("/orders/getRestaurantName")
    public List<String> getRestaurantName(@Valid @RequestBody List<Orders> oids) throws NotFoundException{
        logger.info("Getting Restaurant name");
        logger.info("List of OIDs:" + oids.toString());
        List<String> output = new ArrayList<>();
        for(Orders order : oids){
            output.add(this.getOrdersById(order.getOrderId()).getSeatingTable().getRestaurant().getRestaurantName());
        }
        return output;
    }
}

