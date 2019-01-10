package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.ActivityLogRepository;
import MakaNow.thefirstorder_back.repository.CustomisationOptionRepository;
import MakaNow.thefirstorder_back.repository.CustomisationRepository;
import MakaNow.thefirstorder_back.repository.FoodRepository;
import MakaNow.thefirstorder_back.service.ActivityLogService;
import MakaNow.thefirstorder_back.service.CustomisationService;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import lombok.Data;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class CustomisationController {

    Logger logger = LoggerFactory.getLogger(CustomisationController.class);

    @Autowired
    private CustomisationRepository customisationRepository;

    @Autowired
    private CustomisationService customisationService;

    @Autowired
    private CustomisationOptionRepository customisationOptionRepository ;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodPriceController foodPriceController;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping("/customisations")
    @JsonView(View.CustomisationView.class)
    public List<Customisation> getAllCustomisations() {return (List<Customisation>) customisationRepository.findAll();}

    @GetMapping("/customisation/menu/{menuId}/food/{foodId}/category/{categoryId}")
    @JsonView(View.CustomisationView.class)
    public List<Customisation> getCustomisationByMenuFoodCatId(@PathVariable String menuId,
                                                               @PathVariable String foodId,
                                                               @PathVariable String categoryId) throws NotFoundException {
        FoodPrice foodPrice = foodPriceController.getFoodPriceByMenuFoodCatId(menuId, foodId, categoryId);
        return foodPrice.getCustomisations();
    }

    @PostMapping("/customisation/addCustomisation")
    public ResponseEntity<?> addCustomisation(@RequestBody UpdatedCustomisation updatedCustomisation) {

        String customisationId = customisationService.getNewCustomisationId();
        String customisationName = updatedCustomisation.getCustomisationName();
        String menuId = updatedCustomisation.getMenuId();
        String foodId = updatedCustomisation.getFoodId();
        String categoryId = updatedCustomisation.getCategoryId();
        MenuFoodCatId menuFoodCatId = new MenuFoodCatId(menuId, foodId, categoryId);
        String managerId = updatedCustomisation.getManagerId();
        String restaurantId = updatedCustomisation.getRestaurantId();

        Customisation customisation = new Customisation();
        customisation.setCustomisationId(customisationId);
        customisation.setCustomisationName(customisationName);
        customisation.setMenuFoodCatId(menuFoodCatId);

        customisationRepository.save(customisation);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(managerId);
        activityLog.setRestaurantId(restaurantId);

        String description = "Added '" + customisationName + "' to '" + foodRepository.findById(foodId).get().getFoodName() + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Customisation added successfully", HttpStatus.OK);
    }

    @DeleteMapping("/customisation/deleteCustomisation")
    public ResponseEntity<?> deleteCustomisationByCustomisationId(@RequestParam("customisationId") String customisationId, @RequestParam("managerId") String managerId, @RequestParam("foodId") String foodId, @RequestParam("menuId") String menuId, @RequestParam("restaurantId") String restaurantId){

        String customisationName = customisationRepository.findById(customisationId).get().getCustomisationName();
        String foodName = foodRepository.findById(foodId).get().getFoodName();

        List<CustomisationOption> customisationOptions = customisationRepository.findById(customisationId).get().getCustomisationOptions();

        for(int i = 0; i < customisationOptions.size(); i++){
            String customisationOptionId = customisationOptions.get(i).getCustomisationOptionId();
            customisationOptionRepository.deleteById(customisationOptionId);
        }

        customisationRepository.deleteById(customisationId);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(managerId);
        activityLog.setRestaurantId(restaurantId);

        String description = "Deleted customisation '" + customisationName + "' from '" + foodName + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Deleted Customisation Successfully", HttpStatus.OK);
    }
}
@Data
class UpdatedCustomisation{
    private String customisationName;
    private String categoryId;
    private String menuId;
    private String foodId;
    private String managerId;
    private String restaurantId;
}