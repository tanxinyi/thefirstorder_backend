package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Customer;
import MakaNow.thefirstorder_back.model.CustomerOrder;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.CustomerOrderRepository;
import MakaNow.thefirstorder_back.repository.FoodRepository;
import MakaNow.thefirstorder_back.repository.OrdersRepository;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerOrderController {

    private final String PREFIX = "CUSO";
    private final String FIRST_ID = "CUSO001";

    @Autowired
    private FoodController foodController;

    @Autowired
    private OrdersController ordersController;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    private Logger logger = LoggerFactory.getLogger(CustomerOrderController.class);

    @GetMapping("/customer_orders")
    public Iterable<CustomerOrder> getAllCustomerOrders(){
        return customerOrderRepository.findAll();
    }

    private String getLatestCUSOID(){
        Iterable<CustomerOrder> customerOrders = customerOrderRepository.findAll();

        String latestCUSOID = FIRST_ID;
        for( CustomerOrder customerOrder : customerOrders){
            String currentCUSOID = customerOrder.getCustomerOrderId();
            if(latestCUSOID.equals("") || currentCUSOID.compareTo(latestCUSOID) > 0){
                latestCUSOID = currentCUSOID;
            }
        }
        return latestCUSOID;
    }

    @PostMapping("/customerOrder")
    @JsonView(View.ViewA.class)
    public CustomerOrder save(@Valid @RequestBody CustomerOrder customerOrder) throws NotFoundException {
        logger.info("Saving Customer Order");
        String latestCUSOID = getLatestCUSOID();
        String newCount = "" + (Integer.parseInt(latestCUSOID.substring(PREFIX.length())) + 1);
        latestCUSOID = PREFIX;
        for(int i=0; i<(FIRST_ID.length()-PREFIX.length()-newCount.length()); i++){
            latestCUSOID += "0";
        }
        latestCUSOID += newCount;
        String foodId = customerOrder.getFoodId();
        String orderId = customerOrder.getOrderId();
        customerOrder.setCustomerOrderId(latestCUSOID);
        customerOrder.setOrderId(orderId);
        customerOrder.setFoodId(foodId);
        customerOrder.setFood(foodController.getFoodById(foodId));
        customerOrder.setOrder(ordersController.getOrdersById(orderId));
        return customerOrderRepository.save(customerOrder);
    }

    @PostMapping("/customerOrders")
    public List<CustomerOrder> save(@Valid @RequestBody List<CustomerOrder> customerOrders) throws NotFoundException{
        logger.info("Saving Customer Orders");
        List<CustomerOrder> output = new ArrayList<>();
        for(CustomerOrder customerOrder: customerOrders){
            output.add(save(customerOrder));
        }
        return output;
    }
}
