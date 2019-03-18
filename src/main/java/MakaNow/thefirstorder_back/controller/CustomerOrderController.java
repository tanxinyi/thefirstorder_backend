package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.*;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import lombok.Data;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*")
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
    private CustomisationRepository customisationRepository;

    @Autowired
    private FoodRepository foodRepository;

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

    @GetMapping("/customerOrder/{customerOrderId}")
    @JsonView(View.CustomerOrderView.class)
    public CustomerOrder getCustomerOrderById(@PathVariable String customerOrderId) throws NotFoundException {
        Optional<CustomerOrder> customerOrder = customerOrderRepository.findById(customerOrderId);
        if(customerOrder.isPresent()){
            return customerOrder.get();
        }else{
            throw new NotFoundException("Customer Order Id " + customerOrderId + " does not exist!");
        }
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
        //logger.info(customerOrder.toString());
        String latestCUSOID = getLatestCUSOID();
        String newCount = "" + (Integer.parseInt(latestCUSOID.substring(PREFIX.length())) + 1);
        latestCUSOID = PREFIX;
        for(int i=0; i<(FIRST_ID.length()-PREFIX.length()-newCount.length()); i++){
            latestCUSOID += "0";
        }
        latestCUSOID += newCount;

        MenuFoodCatId menuFoodCatId = customerOrder.getMenuFoodCatId();
        logger.info(menuFoodCatId.toString());

        String orderId = customerOrder.getOrderId();

        String remarks = customerOrder.getCustomerOrderRemarks();
        if(remarks == null) remarks = "";

        double price = customerOrder.getCustomerOrderPrice();

        Orders order = ordersController.getOrdersById(orderId);

        order.setTotalAmount(order.getTotalAmount() + price);
        order.setOrderStatus("SENT");
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

    @GetMapping("/customerOrder/getCustomerOrderByOrderId/{orderId}")
    public List<SpecificFoodOrdered> getCustomerOrderByOrderId(@PathVariable String orderId) {
        List<CustomerOrder> customerOrders = (List) customerOrderRepository.findAll();
        List<SpecificFoodOrdered> toReturn = new ArrayList<>();
        for(int i = 0; i < customerOrders.size(); i++){
            CustomerOrder customerOrder = customerOrders.get(i);
            if(customerOrder.getOrderId().equals(orderId)){
                SpecificFoodOrdered specificFoodOrdered = new SpecificFoodOrdered();
                specificFoodOrdered.setFoodName(foodRepository.findById(customerOrder.getMenuFoodCatId().getFoodId()).get().getFoodName());
                specificFoodOrdered.setCustomerOrderQuantity(customerOrder.getCustomerOrderQuantity());
                specificFoodOrdered.setCustomerOrderPrice(customerOrder.getCustomerOrderPrice());
                specificFoodOrdered.setRemarks(customerOrder.getCustomerOrderRemarks());

                List<CustomisationOption> customisationOptions = customerOrder.getCustomisationOptions();
                List<String> customisationDecriptions = new ArrayList<>();
                for(int k = 0; k < customisationOptions.size(); k++){
                    CustomisationOption customisationOption = customisationOptions.get(k);
                    String customisationName = customisationRepository.findById(customisationOption.getCustomisationId()).get().getCustomisationName();
                    String customisationOptionName = customisationOption.getOptionDescription();
                    String customisationDescription = customisationName + ": " + customisationOptionName;
                    customisationDecriptions.add(customisationDescription);
                }
                specificFoodOrdered.setCustomisations(customisationDecriptions);
                toReturn.add(specificFoodOrdered);
            }
        }
        return toReturn;
    }
}

@Data
class SpecificFoodOrdered{
    private String foodName;
    private int customerOrderQuantity;
    private double customerOrderPrice;
    private String remarks;
    private List<String> customisations;
}
