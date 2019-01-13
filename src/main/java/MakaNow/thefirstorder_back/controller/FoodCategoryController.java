package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.ActivityLog;
import MakaNow.thefirstorder_back.model.FoodCategory;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.ActivityLogRepository;
import MakaNow.thefirstorder_back.repository.FoodCategoryRepository;
import MakaNow.thefirstorder_back.service.ActivityLogService;
import MakaNow.thefirstorder_back.service.FoodCategoryService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class FoodCategoryController {

    @Autowired
    private FoodCategoryRepository foodCategoryRepository;

    @Autowired
    private FoodCategoryService foodCategoryService;

    @PostMapping("/categories")
    @JsonView(View.CategoryView.class)
    public List<FoodCategory> getAllCategories(){ return (List<FoodCategory>) foodCategoryRepository.findAll();
    }

    @PostMapping("/categories/getCategoriesByMenuId/{menuId}")
    @JsonView(View.CategoryView.class)
    public List<FoodCategory> getCategoriesByMenuId(@PathVariable("menuId") String menuId){
        List<FoodCategory> toReturn = foodCategoryService.getCategoriesByMenuId(menuId);
        return toReturn;
    }

    @PostMapping("/categories/addCategory/{menuId}")
    @JsonView(View.CategoryView.class)
    public ResponseEntity<?> addCategory( @PathVariable("menuId") String menuId, @RequestBody NewCategory newCategory){

        String foodCategoryId = foodCategoryService.getNewFoodCategoryId();
        String foodCategoryName = newCategory.getFoodCategoryName();
        String foodCategoryImage = newCategory.getFoodCategoryImg();

        byte[] foodImgByte = foodCategoryImage.getBytes();

        FoodCategory foodCategory = new FoodCategory();
        foodCategory.setFoodCategoryId(foodCategoryId);
        foodCategory.setFoodCategoryName(foodCategoryName);
        foodCategory.setFoodCategoryImgPath(foodImgByte);

        foodCategoryRepository.save(foodCategory);

//        ActivityLog activityLog = new ActivityLog();
//
//        String newActivityLogId = activityLogService.getNewActivityLogId();
//        activityLog.setActivityLogId(newActivityLogId);
//        activityLog.setManagerId(managerId);
//        activityLog.setRestaurantId(restaurantId);
//
//        String description = "Added '" + optionDescription + "' to '" + customisationRepository.findById(customisationId).get().getCustomisationName() + "'";
//        activityLog.setDescription(description);
//
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        activityLog.setChangeTimeStamp(timestamp);

        return new ResponseEntity( foodCategoryId, HttpStatus.OK);
    }

}

@Data
class NewCategory{
    private String foodCategoryName;
    private String foodCategoryImg;
}

