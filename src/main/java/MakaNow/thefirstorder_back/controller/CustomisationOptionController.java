package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.ActivityLog;
import MakaNow.thefirstorder_back.model.CustomisationOption;
import MakaNow.thefirstorder_back.repository.ActivityLogRepository;
import MakaNow.thefirstorder_back.repository.CustomisationOptionRepository;
import MakaNow.thefirstorder_back.repository.CustomisationRepository;
import MakaNow.thefirstorder_back.service.ActivityLogService;
import MakaNow.thefirstorder_back.service.CustomisationOptionService;
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
public class CustomisationOptionController {

    Logger logger = LoggerFactory.getLogger(CustomisationOptionController.class);

    @Autowired
    private CustomisationRepository customisationRepository;

    @Autowired
    private CustomisationOptionRepository customisationOptionRepository;

    @Autowired
    private CustomisationOptionService customisationOptionService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @GetMapping("/customisationOptions")
    public List<CustomisationOption> getAllCustomisationOptions() {
        return (List<CustomisationOption>) customisationOptionRepository.findAll();
    }

    @GetMapping("/customisationOption/{customisationOptionId}")
    public CustomisationOption getCustomisationOptionById(@PathVariable String customisationOptionId) throws NotFoundException {
        logger.info("Getting Customisation Option by Customisation Option Id");
        Optional<CustomisationOption> customisationOptionOptional = customisationOptionRepository.findById(customisationOptionId);
        if(customisationOptionOptional.isPresent()){
            return customisationOptionOptional.get();
        }
        throw new NotFoundException("Customisation Option ID: " + customisationOptionId + " does not exist!");
    }

    @PostMapping("/customisationOption/addCustomisationOption")
    public ResponseEntity<?> addCustomisationOption(@RequestBody UpdatedCustomisationOption updatedCustomisationOption) {

        String customisationOptionId = customisationOptionService.getNewCustomisationOptionId();
        String customisationId = updatedCustomisationOption.getCustomisationId();
        String optionDescription = updatedCustomisationOption.getOptionDescription();
        double optionPrice = updatedCustomisationOption.getOptionPrice();
        String menuId = updatedCustomisationOption.getMenuId();
        String managerId = updatedCustomisationOption.getManagerId();
        String restaurantId = updatedCustomisationOption.getRestaurantId();

        CustomisationOption newCustomisationOption = new CustomisationOption();
        newCustomisationOption.setCustomisationOptionId(customisationOptionId);
        newCustomisationOption.setCustomisationId(customisationId);
        newCustomisationOption.setOptionDescription(optionDescription);
        newCustomisationOption.setOptionPrice(optionPrice);
        customisationOptionRepository.save(newCustomisationOption);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(managerId);
        activityLog.setRestaurantId(restaurantId);

        String description = "Added '" + optionDescription + "' to '" + customisationRepository.findById(customisationId).get().getCustomisationName() + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Customisation option added successfully", HttpStatus.OK);
    }

    @PostMapping("/customisationOption/updateCustomisationOption")
    public ResponseEntity<?> updateCustomisationOption(@RequestBody UpdatedCustomisationOption updatedCustomisationOption) {

        String customisationOptionId = updatedCustomisationOption.getCustomisationOptionId();
        String optionDescription = updatedCustomisationOption.getOptionDescription();
        double optionPrice = updatedCustomisationOption.getOptionPrice();

        CustomisationOption customisationOption = customisationOptionRepository.findById(customisationOptionId).get();
        String oldCustomisationOptionDescription = customisationOption.getOptionDescription();
        String customisationName = customisationRepository.findById(customisationOptionRepository.findById(customisationOptionId).get().getCustomisationId()).get().getCustomisationName();

        customisationOption.setOptionDescription(optionDescription);
        customisationOption.setOptionPrice(optionPrice);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(updatedCustomisationOption.getManagerId());
        activityLog.setRestaurantId(updatedCustomisationOption.getRestaurantId());

        String description = "Updated '" + oldCustomisationOptionDescription + "' in '" + customisationName + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Customisation option updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/customisationOption/deleteCustomisationOption")
    public ResponseEntity<?> deleteCustomisationOptionByCustomisationOptionId( @RequestParam("customisationOptionId") String customisationOptionId, @RequestParam("managerId") String managerId, @RequestParam("menuId") String menuId, @RequestParam("restaurantId") String restaurantId){
        String customisationOptionName = customisationOptionRepository.findById(customisationOptionId).get().getOptionDescription();
        String customisationName = customisationRepository.findById(customisationOptionRepository.findById(customisationOptionId).get().getCustomisationId()).get().getCustomisationName();

        customisationOptionRepository.deleteById(customisationOptionId);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(managerId);
        activityLog.setRestaurantId(restaurantId);

        String description = "Deleted customisation option '" + customisationOptionName + "' from '" + customisationName + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Deleted Customisation Option Successfully", HttpStatus.OK);
    }

    @PostMapping("/getNewOptionId")
    public String getNewOptionID(){
        return customisationOptionService.getNewCustomisationOptionId();
    }
}
@Data
class UpdatedCustomisationOption{
    private String customisationOptionId;
    private String customisationId;
    private String optionDescription;
    private double optionPrice;
    private String restaurantId;
    private String menuId;
    private String managerId;
}
