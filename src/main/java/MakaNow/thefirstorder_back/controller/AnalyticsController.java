package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.CustomerOrder;
import MakaNow.thefirstorder_back.model.FoodPrice;
import MakaNow.thefirstorder_back.model.Orders;
import MakaNow.thefirstorder_back.repository.FoodPriceRepository;
import MakaNow.thefirstorder_back.repository.FoodRepository;
import MakaNow.thefirstorder_back.repository.OrdersRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.time.YearMonth;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AnalyticsController {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodPriceRepository foodPriceRepository;

    Logger logger = LoggerFactory.getLogger(AnalyticsController.class);

    @GetMapping("/analytics/getTotalRevenue/{restaurantId}")
    public double getTotalRevenueOfRestaurant(@PathVariable String restaurantId) {
        double totalRevenue = 0;

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId)){
                ordersOfRestaurant.add(order);
            }
        }

        //Calculate monthly revenue (paid orders) of restaurant based on date
        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            if(order1.getPaymentStatus().equals("Paid")){
                totalRevenue += order1.getTotalAmount();
            }
        }

        return totalRevenue;
    }

    @GetMapping("/analytics/getMonthlyRevenue/{restaurantId}/{date}")
    //Format of String date = YYYY-MM
    public double getMonthlyRevenueOfRestaurant(@PathVariable String restaurantId, @PathVariable String date) {
        double monthlyRevenue = 0;

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId)){
                ordersOfRestaurant.add(order);
            }
        }

        //Calculate monthly revenue (paid orders) of restaurant based on date
        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            if(order1.getOrderDate().toString().substring(0,7).equals(date) && order1.getPaymentStatus().equals("Paid")){
                monthlyRevenue += order1.getTotalAmount();
            }
        }

        return monthlyRevenue;
    }

    @GetMapping("/analytics/getPercentageMonthlyRevenueCompareToPreviousMonth/{restaurantId}/{date}")
    //Format of String date = YYYY-MM
    public double getPercentageMonthlyRevenueCompareToPreviousMonth(@PathVariable String restaurantId, @PathVariable String date) {
        String previousMonthYYYY_MMFormat;
        int thisYear = Integer.parseInt(date.substring(0,4));
        int thisMonth = Integer.parseInt(date.substring(5));
        int previousMonth = thisMonth - 1;

        if (previousMonth == 0){
            int previousYear = thisYear - 1;
            previousMonthYYYY_MMFormat = "" + previousYear + "-12";
        }else if(previousMonth > 0 && previousMonth < 10){
            previousMonthYYYY_MMFormat = "" + thisYear + "-0" + previousMonth;
        }else{
            previousMonthYYYY_MMFormat = "" + thisYear + "-" + previousMonth;
        }

        double thisMonthRevenue = 0;
        double previousMonthRevenue = 0;

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId)){
                ordersOfRestaurant.add(order);
            }
        }

        //Calculate this month monthly revenue (paid orders) of restaurant based on date
        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            if(order1.getOrderDate().toString().substring(0,7).equals(date) && order1.getPaymentStatus().equals("Paid")){
                thisMonthRevenue += order1.getTotalAmount();
            }
        }

        //Calculate previous month monthly revenue (paid orders) of restaurant based on date
        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            if(order1.getOrderDate().toString().substring(0,7).equals(previousMonthYYYY_MMFormat) && order1.getPaymentStatus().equals("Paid")){
                previousMonthRevenue += order1.getTotalAmount();
            }
        }
        if(previousMonthRevenue == 0){
            return 0;
        }else{
            return (thisMonthRevenue/(previousMonthRevenue/100))-100;
        }
    }

    @GetMapping("/analytics/getMonthlyTotalOfFoodItemsSold/{restaurantId}/{date}")
    //Format of String date = YYYY-MM
    public int getMonthlyTotalOfFoodItemsSold(@PathVariable String restaurantId, @PathVariable String date) {
        int totalFoodItemsSold = 0;

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId)){
                ordersOfRestaurant.add(order);
            }
        }

        List<CustomerOrder> customerOrders;
        //Calculate monthly food items sold of restaurant based on date
        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            if(order1.getOrderDate().toString().substring(0,7).equals(date) && order1.getPaymentStatus().equals("Paid")){
                customerOrders = order1.getCustomerOrders();
                for(int k = 0; k < customerOrders.size(); k++){
                    CustomerOrder customerOrder = customerOrders.get(k);
                    totalFoodItemsSold += customerOrder.getCustomerOrderQuantity();
                }
            }
        }

        return totalFoodItemsSold;
    }

    @GetMapping("/analytics/getDailyRevenue/{restaurantId}/{date}")
    //Format of String date = YYYY-MM-DDXXXXXXXXXXXXXXXXXX
    public double getDailyRevenueOfRestaurant(@PathVariable String restaurantId, @PathVariable String date) {
        String dateYYYY_MM_DDFormat = date.substring(0,10);
        double dailyRevenue = 0;

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId)){
                ordersOfRestaurant.add(order);
            }
        }

        //Calculate daily revenue (paid orders) of restaurant based on date
        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            if(order1.getOrderDate().toString().substring(0,10).equals(dateYYYY_MM_DDFormat) && order1.getPaymentStatus().equals("Paid")){
                dailyRevenue += order1.getTotalAmount();
            }
        }

        return dailyRevenue;
    }

    @GetMapping("/analytics/getPercentageDailyRevenueCompareToPreviousDate/{restaurantId}/{date}")
    //Format of String date = YYYY-MM-DDXXXXXXXXXXXXXXXXXX
    public double getPercentageDailyRevenueCompareToPreviousDate(@PathVariable String restaurantId, @PathVariable String date) {
        String previousDate;
        String todayYYYY_MM_DDFormat = date.substring(0,10);
        int thisYear = Integer.parseInt(date.substring(0,4));
        int thisMonth = Integer.parseInt(date.substring(5,7));
        int thisDay = Integer.parseInt(date.substring(8,10));

        int previousDay = thisDay - 1;

        if(previousDay  == 0 && thisMonth == 1){
            previousDate = "" + (thisYear - 1) + "-12-31";
        }else if(previousDay == 0){
            int previousMonth = thisMonth - 1;
            YearMonth yearMonthObject = YearMonth.of(thisYear, previousMonth);
            int daysInMonth = yearMonthObject.lengthOfMonth();
            if(previousMonth > 0 && previousMonth < 10){
                previousDate = "" + thisYear + "-0" + previousMonth + "-" + daysInMonth;
            }else{
                previousDate = "" + thisYear + "-" + previousMonth + "-" + daysInMonth;
            }
        }else{
            if(previousDay > 0 && previousDay < 10){
                if(thisMonth > 0 && thisMonth < 10){
                    previousDate = "" + thisYear + "-0" + thisMonth + "-0" + previousDay;
                }else{
                    previousDate = "" + thisYear + "-" + thisMonth + "-0" + previousDay;
                }
            }else{
                if(thisMonth > 0 && thisMonth < 10){
                    previousDate = "" + thisYear + "-0" + thisMonth + "-" + previousDay;
                }else{
                    previousDate = "" + thisYear + "-" + thisMonth + "-" + previousDay;
                }
            }
        }

        double todayRevenue = 0;
        double previousDayRevenue = 0;

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId)){
                ordersOfRestaurant.add(order);
            }
        }

        //Calculate today revenue (paid orders) of restaurant based on date
        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            if(order1.getOrderDate().toString().substring(0,10).equals(todayYYYY_MM_DDFormat) && order1.getPaymentStatus().equals("Paid")){
                todayRevenue += order1.getTotalAmount();
            }
        }

        //Calculate previous day revenue (paid orders) of restaurant based on date
        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            if(order1.getOrderDate().toString().substring(0,10).equals(previousDate) && order1.getPaymentStatus().equals("Paid")){
                previousDayRevenue += order1.getTotalAmount();
            }
        }

        if(previousDayRevenue == 0){
            return 0;
        }else{
            return (todayRevenue/(previousDayRevenue/100))-100;
        }
    }

    @GetMapping("/analytics/getDailyTotalOfFoodItemsSold/{restaurantId}/{date}")
    //Format of String date = YYYY-MM-DDXXXXXXXXXXXXXXXXXX
    public double getDailyTotalOfFoodItemsSold(@PathVariable String restaurantId, @PathVariable String date) {
        String dateYYYY_MM_DDFormat = date.substring(0,10);
        double totalDailyFoodItemsSold = 0;

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId)){
                ordersOfRestaurant.add(order);
            }
        }

        List<CustomerOrder> customerOrders;
        //Calculate daily revenue (paid orders) of restaurant based on date
        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            if(order1.getOrderDate().toString().substring(0,10).equals(dateYYYY_MM_DDFormat) && order1.getPaymentStatus().equals("Paid")){
                customerOrders = order1.getCustomerOrders();
                for(int k = 0; k < customerOrders.size(); k++){
                    CustomerOrder customerOrder = customerOrders.get(k);
                    totalDailyFoodItemsSold += customerOrder.getCustomerOrderQuantity();
                }
            }
        }

        return totalDailyFoodItemsSold;
    }

    @GetMapping("/analytics/getMonthlyRevenueBreakdownByDays/{restaurantId}/{date}")
    //Format of String date = YYYY-MM
    public TreeMap<String, Double> getMonthlyRevenueBreakdownByDays(@PathVariable String restaurantId, @PathVariable String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5));

        // Get the number of days in that month
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();

        TreeMap<String, Double> toReturn = new TreeMap<>();

        for(int k = 0; k < daysInMonth; k++){
            String date1 = "";
            if(k+1 < 10){
                date1 = date + "-0" + (k+1);
            }else{
                date1 = date + "-" + (k+1);
            }
            toReturn.put(date1, 0.00);
        }

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant in specified month
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId) && order.getOrderDate().toString().substring(0,7).equals(date) && order.getPaymentStatus().equals("Paid")){
                ordersOfRestaurant.add(order);
            }
        }

        //Get daily revenue and put into map
        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            String orderDate = order1.getOrderDate().toString().substring(0,10);
            double dailyRevenue = toReturn.get(orderDate);
            double orderAmount = order1.getTotalAmount();
            toReturn.put(orderDate, dailyRevenue+orderAmount);
        }

        return toReturn;
    }

    @GetMapping("/analytics/getDailyRevenueBreakdownByHour/{restaurantId}/{date}")
    //Example format of String date = YYYY-MM-DD
    public TreeMap<String, Double> getDailyRevenueBreakdownByHour(@PathVariable String restaurantId, @PathVariable String date) {
        TreeMap<String, Double> toReturn = new TreeMap<>();

        for(int i = 0; i < 24; i++){
            String timeHour = "";
            if(i == 0){
                timeHour = "" + "00:00";
            }else if (i > 0 && i < 10){
                timeHour = "0" + i + ":00";
            }else{
                timeHour = i + ":00";
            }
            toReturn.put(timeHour, 0.00);
        }

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant in specified day
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId) && order.getOrderDate().toString().substring(0,10).equals(date) && order.getPaymentStatus().equals("Paid")){
                ordersOfRestaurant.add(order);
            }
        }

        //Get hourly revenue and put into map
        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            Date orderDate = ordersRepository.findById(order1.getOrderId()).get().getOrderDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
            String formatted = sdf.format(orderDate);
            String hour = formatted.substring(11,13) + ":00";
            double hourlyRevenue = toReturn.get(hour);
            double orderAmount = order1.getTotalAmount();
            toReturn.put(hour, hourlyRevenue+orderAmount);
        }

        return toReturn;
    }

    @GetMapping("/analytics/getMonthlyRevenueByFoodItem/{restaurantId}/{foodId}/{date}")
    //Format of String date = YYYY-MM
    public double getMonthlyFoodItemRevenueOfRestaurant(@PathVariable String restaurantId, @PathVariable String foodId, @PathVariable String date) {
        double monthlyRevenueOfFoodItem = 0;

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId)){
                ordersOfRestaurant.add(order);
            }
        }

        List<CustomerOrder> customerOrders;

        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            if(order1.getOrderDate().toString().substring(0,7).equals(date) && order1.getPaymentStatus().equals("Paid")){
                customerOrders = order1.getCustomerOrders();
                if(customerOrders != null){
                    for(int k = 0; k < customerOrders.size(); k++){
                        CustomerOrder customerOrder = customerOrders.get(k);
                        if(customerOrder.getMenuFoodCatId().getFoodId().equals(foodId)){
                            monthlyRevenueOfFoodItem += customerOrder.getCustomerOrderPrice();
                        }
                    }
                }
            }
        }

        return monthlyRevenueOfFoodItem;
    }



    @GetMapping("/analytics/getDailyRevenueByFoodItem/{restaurantId}/{foodId}/{date}")
    //Format of String date = YYYY-MM-DDXXXXXXXXXXXXXXXXXX
    public double getDailyFoodItemRevenueOfRestaurant(@PathVariable String restaurantId, @PathVariable String foodId, @PathVariable String date) {
        String dateYYYY_MM_DDFormat = date.substring(0,10);
        double dailyRevenueOfFoodItem = 0;

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId)){
                ordersOfRestaurant.add(order);
            }
        }

        List<CustomerOrder> customerOrders;

        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            if(order1.getOrderDate().toString().substring(0,10).equals(dateYYYY_MM_DDFormat) && order1.getPaymentStatus().equals("Paid")){
                customerOrders = order1.getCustomerOrders();
                if(customerOrders != null){
                    for(int k = 0; k < customerOrders.size(); k++){
                        CustomerOrder customerOrder = customerOrders.get(k);
                        if(customerOrder.getMenuFoodCatId().getFoodId().equals(foodId)){
                            dailyRevenueOfFoodItem += customerOrder.getCustomerOrderPrice();
                        }
                    }
                }
            }
        }

        return dailyRevenueOfFoodItem;
    }

    @GetMapping("/analytics/getMonthlyRevenueBreakdownByFoodItem/{restaurantId}/{date}")
    //Format of String date = YYYY-MM
    public Map<String, Double> getMonthlyRevenueBreakdownByFoodItem(@PathVariable String restaurantId, @PathVariable String date) {

        Map<String, Double> toReturn = new HashMap<String, Double>();

        List<FoodPrice> foodPrices = (List<FoodPrice>) foodPriceRepository.findAll();

        for(int k = 0; k < foodPrices.size(); k++){
            FoodPrice foodPrice = foodPrices.get(k);
            if(foodPrice.getMenu().getRestaurantId().equals(restaurantId)){
                toReturn.put(foodPrice.getFood().getFoodName(), 0.00);
            }
        }

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant in specified month
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId) && order.getOrderDate().toString().substring(0,7).equals(date) && order.getPaymentStatus().equals("Paid")){
                ordersOfRestaurant.add(order);
            }
        }

        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            List<CustomerOrder> customerOrders = order1.getCustomerOrders();
            for(int l = 0; l < customerOrders.size(); l++){
                CustomerOrder customerOrder = customerOrders.get(l);
                String foodName = foodRepository.findById(customerOrder.getMenuFoodCatId().getFoodId()).get().getFoodName();
                double foodMonthlyRevenue = toReturn.get(foodName);
                double foodOrderRevenue = customerOrder.getCustomerOrderPrice();
                toReturn.put(foodName, foodMonthlyRevenue+foodOrderRevenue);
            }
        }

        return sortByValue(toReturn);
    }

    @GetMapping("/analytics/getMonthlyRevenuePercentageBreakdownByFoodItem/{restaurantId}/{date}")
    //Format of String date = YYYY-MM
    public Map<String, Double> getMonthlyRevenuePercentageBreakdownByFoodItem(@PathVariable String restaurantId, @PathVariable String date) {
        double monthlyTotalRevenue = getMonthlyRevenueOfRestaurant(restaurantId, date);
        Map<String, Double> toReturn = new HashMap<String, Double>();

        List<FoodPrice> foodPrices = (List<FoodPrice>) foodPriceRepository.findAll();

        for(int k = 0; k < foodPrices.size(); k++){
            FoodPrice foodPrice = foodPrices.get(k);
            if(foodPrice.getMenu().getRestaurantId().equals(restaurantId)){
                toReturn.put(foodPrice.getFood().getFoodName(), 0.00);
            }
        }

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant in specified month
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId) && order.getOrderDate().toString().substring(0,7).equals(date) && order.getPaymentStatus().equals("Paid")){
                ordersOfRestaurant.add(order);
            }
        }

        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            List<CustomerOrder> customerOrders = order1.getCustomerOrders();
            for(int l = 0; l < customerOrders.size(); l++){
                CustomerOrder customerOrder = customerOrders.get(l);
                String foodName = foodRepository.findById(customerOrder.getMenuFoodCatId().getFoodId()).get().getFoodName();
                double foodMonthlyRevenue = toReturn.get(foodName);
                double foodOrderRevenue = customerOrder.getCustomerOrderPrice();
                foodMonthlyRevenue += foodOrderRevenue;
                toReturn.put(foodName, foodMonthlyRevenue);
            }
        }

        toReturn.replaceAll((key, oldValue) -> (oldValue/monthlyTotalRevenue)*100);

        return sortByValue(toReturn);
    }

    @GetMapping("/analytics/getDailyRevenueBreakdownByFoodItem/{restaurantId}/{date}")
    //Format of String date = YYYY-MM-DDXXXXXXXXXXXXXXXXXX
    public Map<String, Double> getDailyRevenueBreakdownByFoodItem(@PathVariable String restaurantId, @PathVariable String date) {
        String dateYYYY_MM_DDFormat = date.substring(0,10);
        Map<String, Double> toReturn = new HashMap<String, Double>();

        List<FoodPrice> foodPrices = (List<FoodPrice>) foodPriceRepository.findAll();

        for(int k = 0; k < foodPrices.size(); k++){
            FoodPrice foodPrice = foodPrices.get(k);
            if(foodPrice.getMenu().getRestaurantId().equals(restaurantId)){
                toReturn.put(foodPrice.getFood().getFoodName(), 0.00);
            }
        }

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant in specified month
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId) && order.getOrderDate().toString().substring(0,10).equals(dateYYYY_MM_DDFormat) && order.getPaymentStatus().equals("Paid")){
                ordersOfRestaurant.add(order);
            }
        }

        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            List<CustomerOrder> customerOrders = order1.getCustomerOrders();
            for(int l = 0; l < customerOrders.size(); l++){
                CustomerOrder customerOrder = customerOrders.get(l);
                String foodName = foodRepository.findById(customerOrder.getMenuFoodCatId().getFoodId()).get().getFoodName();
                double foodDailyRevenue = toReturn.get(foodName);
                double foodOrderRevenue = customerOrder.getCustomerOrderPrice();
                toReturn.put(foodName, foodDailyRevenue+foodOrderRevenue);
            }
        }

        return sortByValue(toReturn);
    }

    @GetMapping("/analytics/getDailyRevenuePercentageBreakdownByFoodItem/{restaurantId}/{date}")
    //Format of String date = YYYY-MM-DDXXXXXXXXXXXXXXXXXX
    public Map<String, Double> getDailyRevenuePercentageBreakdownByFoodItem(@PathVariable String restaurantId, @PathVariable String date) {
        double totalDailyRevenue = getDailyRevenueOfRestaurant(restaurantId, date);
        String dateYYYY_MM_DDFormat = date.substring(0,10);
        Map<String, Double> toReturn = new HashMap<String, Double>();

        List<FoodPrice> foodPrices = (List<FoodPrice>) foodPriceRepository.findAll();

        for(int k = 0; k < foodPrices.size(); k++){
            FoodPrice foodPrice = foodPrices.get(k);
            if(foodPrice.getMenu().getRestaurantId().equals(restaurantId)){
                toReturn.put(foodPrice.getFood().getFoodName(), 0.00);
            }
        }

        List<Orders> ordersOfRestaurant = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant in specified month
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId) && order.getOrderDate().toString().substring(0,10).equals(dateYYYY_MM_DDFormat) && order.getPaymentStatus().equals("Paid")){
                ordersOfRestaurant.add(order);
            }
        }

        for(int j = 0; j < ordersOfRestaurant.size(); j++){
            Orders order1 = ordersOfRestaurant.get(j);
            List<CustomerOrder> customerOrders = order1.getCustomerOrders();
            for(int l = 0; l < customerOrders.size(); l++){
                CustomerOrder customerOrder = customerOrders.get(l);
                String foodName = foodRepository.findById(customerOrder.getMenuFoodCatId().getFoodId()).get().getFoodName();
                double foodDailyRevenue = toReturn.get(foodName);
                double foodOrderRevenue = customerOrder.getCustomerOrderPrice();
                toReturn.put(foodName, foodDailyRevenue+foodOrderRevenue);
            }
        }

        toReturn.replaceAll((key, oldValue) -> (oldValue/totalDailyRevenue)*100);

        return sortByValue(toReturn);
    }

    @GetMapping("/analytics/getMonthlyTransactions/{restaurantId}/{date}")
    //Format of String date = YYYY-MM
    public List<Transaction> getMonthlyTransactions(@PathVariable String restaurantId, @PathVariable String date) {
        List<Transaction> toReturn = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant based on specified date
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId) && order.getOrderDate().toString().substring(0,7).equals(date) && order.getPaymentStatus().equals("Paid")){
                Transaction transaction = new Transaction();
                transaction.setOrderId(order.getOrderId());
                transaction.setSeatingTable(order.getSeatingTable().getQrCode());
                transaction.setTotalAmount(order.getTotalAmount());
                transaction.setModeOfPayment(order.getModeOfPayment());
                transaction.setTransactionDateTime(order.getOrderDate().toString());
                toReturn.add(transaction);
            }
        }

        return toReturn;
    }

    @GetMapping("/analytics/getDailyTransactions/{restaurantId}/{date}")
    //Format of String date = YYYY-MM-DDXXXXXXXXXXXXXXXXXX
    public List<Transaction> getDailyTransactions(@PathVariable String restaurantId, @PathVariable String date) {
        String dateYYYY_MM_DDFormat = date.substring(0,10);
        List<Transaction> toReturn = new ArrayList<>();
        List<Orders> orders = (List<Orders>) ordersRepository.findAll();

        //Get all orders of a restaurant based on specified date
        for(int i = 0; i < orders.size(); i++){
            Orders order = orders.get(i);
            if(order.getSeatingTable().getRestaurantId().equals(restaurantId) && order.getOrderDate().toString().substring(0,10).equals(dateYYYY_MM_DDFormat) && order.getPaymentStatus().equals("Paid")){
                Transaction transaction = new Transaction();
                transaction.setOrderId(order.getOrderId());
                transaction.setSeatingTable(order.getSeatingTable().getQrCode());
                transaction.setTotalAmount(order.getTotalAmount());
                transaction.setModeOfPayment(order.getModeOfPayment());
                transaction.setTransactionDateTime(order.getOrderDate().toString());
                toReturn.add(transaction);
            }
        }

        return toReturn;
    }

    //Method to sort values in map by descending order
    public static Map<String, Double> sortByValue(final Map<String, Double> wordCounts) {
        return wordCounts.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Double>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, ( e1, e2) -> e1, LinkedHashMap::new));
    }
}

@Data
class Transaction{
    private String orderId;
    private String seatingTable;
    private double totalAmount;
    private String modeOfPayment;
    private String transactionDateTime;
}

