package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.*;
import MakaNow.thefirstorder_back.service.ActivityLogService;
import MakaNow.thefirstorder_back.service.FoodService;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class FoodPriceController {

    Logger logger = LoggerFactory.getLogger(FoodPriceController.class);

    @Autowired
    private FoodPriceRepository foodPriceRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private FoodService foodService;

    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping("/foodPrice/menuId/{menuId}/foodId/{foodId}/categoryId/{catId}")
    @JsonView(View.FoodPriceView.class)
    public FoodPrice getFoodPriceByMenuFoodCatId( @PathVariable String menuId,
                                                  @PathVariable String foodId,
                                                  @PathVariable String catId) throws NotFoundException {
        logger.info("Getting FoodPrice by MenuFoodCatId");
        Optional<FoodPrice> optionalFoodPrice = foodPriceRepository.findById(new MenuFoodCatId(menuId, foodId, catId));
        if(optionalFoodPrice.isPresent()){
            return optionalFoodPrice.get();
        }else{
            throw new NotFoundException("MenuFoodCatID: MenuID: " + menuId + ", FoodId: " + foodId + ", CategoryId: " + catId + " does not exist");
        }
    }

    @PostMapping("/foodPrices")
    @JsonView(View.FoodPriceView.class)
    public List<FoodPrice> getAllFoodPrices(){
        return (List<FoodPrice>) foodPriceRepository.findAll();
    }

    @PostMapping("/foodPrices/addFoodPrice/{menuId}")
    @JsonView(View.FoodPriceView.class)
    public ResponseEntity<?> addFoodPrice(@PathVariable("menuId") String menuId, @RequestBody FoodItem foodItem) {

        String foodId = foodService.getNewFoodId();
        String foodName = foodItem.getFoodName();
        String foodDescription = foodItem.getFoodDescription();
        String foodCategoryId = foodItem.getFoodCategoryId();
        String subCategoryId = foodItem.getSubCategoryId();
        double foodPrice = foodItem.getFoodPrice();
        boolean foodAvailability = Boolean.parseBoolean(foodItem.getFoodAvailability());
        String foodImg = foodItem.getFoodImg();

        byte[] foodImgByte = foodImg.getBytes();

        Food newFood = new Food();
        newFood.setFoodId(foodId);
        newFood.setFoodName(foodName);
        newFood.setFoodDescription(foodDescription);
        newFood.setFoodImgPath(foodImgByte);
        foodRepository.save(newFood);

        FoodPrice newFoodPrice = new FoodPrice();
        MenuFoodCatId newFoodPricePK = new MenuFoodCatId(menuId, foodId, foodCategoryId);
        newFoodPrice.setMenuFoodCatId(newFoodPricePK);
        if(subCategoryId.equals("1")){
            newFoodPrice.setSubCategoryId(null);
        }else{
            newFoodPrice.setSubCategoryId(subCategoryId);
        }
        newFoodPrice.setFoodPrice(foodPrice);
        newFoodPrice.setAvailability(foodAvailability);

        foodPriceRepository.save(newFoodPrice);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(foodItem.getManagerId());
        activityLog.setRestaurantId(foodItem.getRestaurantId());

        String description = "Added '" + foodName + "' to '" + menuRepository.findById(menuId).get().getMenuName() + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Food Item added successfully", HttpStatus.OK);
    }

    @PostMapping("/foodPrices/updateFoodPrice/{menuId}/{foodId}")
    @JsonView(View.FoodPriceView.class)
    public ResponseEntity<?> updateFoodPrice(@PathVariable("menuId") String menuId, @PathVariable("foodId") String foodId, @RequestBody FoodItem foodItem) {

        String foodName = foodItem.getFoodName();
        String foodDescription = foodItem.getFoodDescription();
        String oldFoodCategoryId = foodItem.getOldFoodCategoryId();
        String subCategoryId = foodItem.getSubCategoryId();
        double foodPrice = foodItem.getFoodPrice();
        boolean foodAvailability = Boolean.parseBoolean(foodItem.getFoodAvailability());
        String foodImg = foodItem.getFoodImg();

        byte[] foodImgByte = foodImg.getBytes();

        String foodCategoryId = foodItem.getFoodCategoryId();

        Food food = foodService.getFoodByFoodId(foodId);
//        FoodPrice foodPriceItem = foodPriceService.getFoodPriceByMenuIdAndFoodId(menuId, foodId, oldFoodCategoryId);

        food.setFoodName(foodName);
        food.setFoodDescription(foodDescription);
//        food.setCategoryId("CAT001");
        food.setFoodImgPath(foodImgByte);
        foodRepository.save(food);

        MenuFoodCatId foodPricePK = new MenuFoodCatId(menuId, foodId, oldFoodCategoryId);
        FoodPrice updatefoodPrice = foodPriceRepository.findById(foodPricePK).get();
//        MenuFoodCatId newFoodPricePK = new MenuFoodCatId(menuId, foodId, foodCategoryId);

//        List<Customisation> customisations = foodPriceRepository.findById(foodPricePK).get().getCustomisations();
//        for(int i = 0; i < customisations.size(); i++){
//            Customisation customisation = customisations.get(i);
//            customisation.setMenuFoodCatId(newFoodPricePK);
//        }
//        foodPriceRepository.deleteById(foodPricePK);

        updatefoodPrice.setFoodPrice(foodPrice);
        updatefoodPrice.setAvailability(foodAvailability);
//        foodPriceItem.setSubCategoryId(subCategoryId);
//        foodPriceItem.setCustomisations(customisations);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(foodItem.getManagerId());
        activityLog.setRestaurantId(foodItem.getRestaurantId());

        String description = "Updated '" + foodName + "' in '" + menuRepository.findById(menuId).get().getMenuName() + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Food Item updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/foodPrices/deleteFoodPrice")
    @JsonView(View.FoodPriceView.class)
    public ResponseEntity<?> deleteFoodPriceByMenuIdAndFoodId( @RequestParam("menuId") String menuId, @RequestParam("foodId") String foodId, @RequestParam("foodCategoryId") String foodCategoryId, @RequestParam("managerId") String managerId, @RequestParam("restaurantId") String restaurantId){
        MenuFoodCatId foodPricePK = new MenuFoodCatId(menuId, foodId, foodCategoryId);

//        List<Customisation> customisations = (List<Customisation>) customisationRepository.findAll();

//        for(int i = 0; i < customisations.size(); i++){
//            Customisation customisation = customisations.get(i);
//            if(customisation.getMenuFoodCatId().getMenuId().equals(menuId) && customisation.getMenuFoodCatId().getFoodId().equals(foodId) && customisation.getMenuFoodCatId().getFoodCategoryId().equals(foodCategoryId)){
//                String customisationId = customisation.getCustomisationId();
//                List<CustomisationOption> customisationOptions = customisationRepository.findById(customisationId).get().getCustomisationOptions();
//                for(int j = 0; j < customisationOptions.size(); j++){
//                    String customisationOptionId = customisationOptions.get(j).getCustomisationOptionId();
//                    customisationOptionRepository.deleteById(customisationOptionId);
//                }
//                customisationRepository.deleteById(customisation.getCustomisationId());
//            }
//        }

        foodPriceRepository.deleteById(foodPricePK);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(managerId);
        activityLog.setRestaurantId(restaurantId);

        Optional<Food> optionalFood = foodRepository.findById(foodId);
        Food food1 = optionalFood.get();

        String description = "Deleted '" + food1.getFoodName() + "' from '" + menuRepository.findById(menuId).get().getMenuName() + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Deleted Food Item Successfully", HttpStatus.OK);
    }

    @DeleteMapping("/foodPrices/deleteFoodPrice/{menuId}/{foodId}/{categoryId}")
    @JsonView(View.FoodPriceView.class)
    public ResponseEntity<?> deleteFoodPriceByMenuIdAndFoodId1(@PathVariable("menuId") String menuId, @PathVariable("foodId") String foodId, @PathVariable("categoryId") String categoryId){
        MenuFoodCatId menuFoodCatId = new MenuFoodCatId(menuId, foodId, categoryId);
//        FoodPrice foodPrice = foodPriceRepository.findById(menuFoodCatId).get();
//
////        foodPriceRepository.findById(menuFoodCatId).get().setCustomisations(null);
//        List<Customisation> customisations = (List<Customisation>) customisationRepository.findAll();
//
//        for(int i = 0; i < customisations.size(); i++){
//            Customisation customisation = customisations.get(i);
//            if (customisation.getMenuFoodCatId().getMenuId().equals(menuId) && customisation.getMenuFoodCatId().getFoodId().equals(foodId)){
////                foodPrice.removeCustomisation(customisation);
//                customisationRepository.deleteById(customisation.getCustomisationId());
//            }
//        }

//        foodPriceRepository.findById(menuFoodCatId).get().setCustomisations(null);

        foodPriceRepository.deleteById(menuFoodCatId);

        return new ResponseEntity("SUCCESS", HttpStatus.OK);
    }
}

@Data
class FoodItem{
    private String foodName;
    private String foodDescription;
    private String oldFoodCategoryId;
    private String foodCategoryId;
    private String subCategoryId;
    private double foodPrice;
    private String foodAvailability;
    private String restaurantId;
    private String menuId;
    private String managerId;
    private String foodImg;
}
