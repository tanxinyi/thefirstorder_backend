package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Food;
import MakaNow.thefirstorder_back.model.FoodPrice;
import MakaNow.thefirstorder_back.repository.FoodRepository;
import MakaNow.thefirstorder_back.service.FoodService;
import javassist.NotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodService foodService;

    @GetMapping("/foodItems")
    public List<Food> getAllFoodItems(){
        return (List<Food>) foodRepository.findAll();
    }

    @GetMapping("/foodItems/{foodId}")
    public Food getFoodById( @PathVariable String foodId ) throws NotFoundException {
        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if(optionalFood.isPresent()){
            return optionalFood.get();
        }else{
            throw new NotFoundException("Menu ID:" + foodId + " does not exist");
        }
    }

    @PostMapping("/foods")
    public List<Food> getAllFoods(){
        return (List<Food>) foodRepository.findAll();
    }

    @GetMapping("/foods/{menuId}")
    public ResponseEntity<?> getFoodItemsByMenuId( @PathVariable("menuId") String menuId ){
        List<FoodItemInMenu> result = new ArrayList<>();
        List<Food> foods = foodService.getFoodItemsByMenuId(menuId);

        for (int i = 0; i < foods.size(); i++){
            Food food = foods.get(i);
            List<FoodPrice> foodPrices = food.getFoodPrices();
            for (int j = 0; j < foodPrices.size(); j++){
                FoodPrice foodPrice = foodPrices.get(j);
                if (foodPrice.getMenuFoodId().getMenuId().equals(menuId)){
                    FoodItemInMenu foodItemInMenu = new FoodItemInMenu();
                    foodItemInMenu.setFoodId(food.getFoodId());
                    foodItemInMenu.setFoodName(food.getName());
                    foodItemInMenu.setFoodDescription(food.getDescription());
                    foodItemInMenu.setFoodCategory("");
                    foodItemInMenu.setFoodPrice(foodPrice.getPrice());
                    foodItemInMenu.setFoodAvailability(foodPrice.isAvailability());
                    result.add(foodItemInMenu);
                }
            }
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/foods/getNewFoodId")
    public String getNewFoodId(){
        return foodService.getNewFoodId();
    }
}

@Data
class FoodItemInMenu{
    private String foodId;
    private String foodName;
    private String foodDescription;
    private String foodCategory;
    private double foodPrice;
    private boolean foodAvailability;

}
