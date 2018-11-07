package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.ActivityLogRepository;
import MakaNow.thefirstorder_back.repository.FoodPriceRepository;
import MakaNow.thefirstorder_back.repository.FoodRepository;
import MakaNow.thefirstorder_back.service.ActivityLogService;
import MakaNow.thefirstorder_back.service.FoodPriceService;
import MakaNow.thefirstorder_back.service.FoodService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class FoodPriceController {

    @Autowired
    private FoodPriceRepository foodPriceRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private FoodService foodService;

    @Autowired
    private FoodPriceService foodPriceService;

    @Autowired
    private ActivityLogService activityLogService;

    @PostMapping("/foodPrices")
    @JsonView(View.ViewA.class)
    public List<FoodPrice> getAllFoodPrices(){
        return (List<FoodPrice>) foodPriceRepository.findAll();
    }

    @PostMapping("/foodPrices/addFoodPrice/{menuId}")
    @JsonView(View.ViewA.class)
    public ResponseEntity<?> addFoodPrice(@PathVariable("menuId") String menuId, @RequestBody FoodItem foodItem) {

        String foodId = foodService.getNewFoodId();
        String foodName = foodItem.getFoodName();
        String foodDescription = foodItem.getFoodDescription();
        String foodCategory = foodItem.getFoodCategory();
        double foodPrice = foodItem.getFoodPrice();
        boolean foodAvailability = Boolean.parseBoolean(foodItem.getFoodAvailability());

        Food newFood = new Food();
        newFood.setFoodId(foodId);
        newFood.setName(foodName);
        newFood.setDescription(foodDescription);
        newFood.setCategoryId("CAT001");
        newFood.setImgPath("");
        foodRepository.save(newFood);

        FoodPrice newFoodPrice = new FoodPrice();
        MenuFoodId newFoodPricePK = new MenuFoodId(menuId, foodId);
        newFoodPrice.setMenuFoodId(newFoodPricePK);
        newFoodPrice.setPrice(foodPrice);
        newFoodPrice.setAvailability(foodAvailability);

        foodPriceRepository.save(newFoodPrice);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(foodItem.getManagerId());
        activityLog.setRestaurantId(foodItem.getRestaurantId());

        String description = "Added Food Item '" + foodId + "' to Menu '" + menuId + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Food Item added succcessfully", HttpStatus.OK);
    }

    @PostMapping("/foodPrices/updateFoodPrice/{menuId}/{foodId}")
    @JsonView(View.ViewA.class)
    public ResponseEntity<?> updateFoodPrice(@PathVariable("menuId") String menuId, @PathVariable("foodId") String foodId, @RequestBody FoodItem foodItem) {

        String foodName = foodItem.getFoodName();
        String foodDescription = foodItem.getFoodDescription();
        String foodCategory = foodItem.getFoodCategory();
        double foodPrice = foodItem.getFoodPrice();
        boolean foodAvailability = Boolean.parseBoolean(foodItem.getFoodAvailability());

        Food food = foodService.getFoodByFoodId(foodId);
        FoodPrice foodPriceItem = foodPriceService.getFoodPriceByMenuIdAndFoodId(menuId, foodId);

        food.setName(foodName);
        food.setDescription(foodDescription);
        food.setCategoryId("CAT001");
        food.setImgPath("");
        foodRepository.save(food);

        foodPriceItem.setPrice(foodPrice);
        foodPriceItem.setAvailability(foodAvailability);
        foodPriceRepository.save(foodPriceItem);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(foodItem.getManagerId());
        activityLog.setRestaurantId(foodItem.getRestaurantId());

        String description = "Edited Food Item '" + foodId + "' in Menu '" + menuId + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Food Item updated succcessfully", HttpStatus.OK);
    }

    @DeleteMapping("/foodPrices")
    @JsonView(View.ViewA.class)
    public ResponseEntity<?> deleteFoodPriceByMenuIdAndFoodId( @RequestParam("menuId") String menuId, @RequestParam("foodId") String foodId, @RequestParam("managerId") String managerId, @RequestParam("restaurantId") String restaurantId){
        MenuFoodId foodPricePK = new MenuFoodId(menuId, foodId);
        foodPriceRepository.deleteById(foodPricePK);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(managerId);
        activityLog.setRestaurantId(restaurantId);

        String description = "Deleted Food Item '" + foodId + "' from Menu '" + menuId + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Deleted Food Item Successfully", HttpStatus.OK);
    }
}

@Data
class FoodItem{
    private String foodName;
    private String foodDescription;
    private String foodCategory;
    private double foodPrice;
    private String foodAvailability;
    private String restaurantId;
    private String menuId;
    private String managerId;
}
