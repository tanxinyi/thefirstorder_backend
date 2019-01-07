package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.*;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CustomerOrderController {

    private final String PREFIX = "CUSO";
    private final String FIRST_ID = "CUSO000";

    @Autowired
    private FoodController foodController;

    @Autowired
    private FoodPriceController foodPriceController;

    @Autowired
    private OrdersController ordersController;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private CustomisationOptionController customisationOptionController;

    @Autowired
    private FoodPriceRepository foodPriceRepository;

    @Autowired
    private OrdersRepository ordersRepository;

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
    @JsonView(View.CustomerOrderView.class)
    public CustomerOrder save(@Valid @RequestBody CustomerOrder customerOrder) throws NotFoundException {
        logger.info("Saving Customer Order");
        String latestCUSOID = getLatestCUSOID();
        String newCount = "" + (Integer.parseInt(latestCUSOID.substring(PREFIX.length())) + 1);
        latestCUSOID = PREFIX;
        for(int i=0; i<(FIRST_ID.length()-PREFIX.length()-newCount.length()); i++){
            latestCUSOID += "0";
        }
        latestCUSOID += newCount;

        MenuFoodCatId menuFoodCatId = customerOrder.getMenuFoodCatId();

        String orderId = customerOrder.getOrderId();

        String remarks = customerOrder.getCustomerOrderRemarks();
        if(remarks == null) remarks = "";

        double price = customerOrder.getCustomerOrderPrice();

        Orders order = ordersController.getOrdersById(orderId);

        order.setSubtotal(order.getSubtotal() + price);
        ordersRepository.save(order);

        FoodPrice foodPrice = foodPriceController
                .getFoodPriceByMenuFoodCatId(
                        menuFoodCatId.getMenuId(),
                        menuFoodCatId.getFoodId(),
                        menuFoodCatId.getFoodCategoryId());

        CustomerOrder newCustomerOrder = new CustomerOrder(latestCUSOID,
                price,
                customerOrder.getCustomerOrderQuantity(),
                remarks,
                order,
                foodPrice,
                new ArrayList<>());

        setCustomisationOptions(newCustomerOrder, customerOrder);

        logger.info("Saving to Repository");
        return customerOrderRepository.save(newCustomerOrder);
    }

    private void setCustomisationOptions(CustomerOrder newCustomerOrder,
                                         CustomerOrder customerOrder) throws NotFoundException {

        logger.info("Setting customisationOptions in CustomerOrder");
        List<CustomisationOption> customisationOptions = customerOrder.getCustomisationOptions();

        if(customisationOptions == null) {
            return;
        }

        for(CustomisationOption customisationOption: customisationOptions){
            CustomisationOption newCustomisationOption = customisationOptionController.getCustomisationOptionById(customisationOption.getCustomisationOptionId());
            newCustomerOrder.getCustomisationOptions().add(newCustomisationOption);
        }
    }

    @PostMapping("/customerOrders")
    @JsonView(View.CustomerOrderView.class)
    public List<CustomerOrder> save(@Valid @RequestBody List<CustomerOrder> customerOrders) throws NotFoundException{
        logger.info("Saving Customer Orders");
        List<CustomerOrder> output = new ArrayList<>();
        for(CustomerOrder customerOrder: customerOrders){
            output.add(save(customerOrder));
        }
        return output;
    }
}
