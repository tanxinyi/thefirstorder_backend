package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Food;
import MakaNow.thefirstorder_back.repository.FoodRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

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
//***NOT DONE WITH MAPPING ASSOCIATIONS***//
//***MANY-TO-ONE RELATIONSHIP WITH FOOD PRICE***//

//    @PostMapping("/foodItems")
//    public Food createFood(@Valid @RequestBody Food food){
//        return foodRepository.save(food);
//    }
//
//    @DeleteMapping("/foodItems/{foodId}")
//    public String deleteFoodItem(@PathVariable String foodId) throws NotFoundException {
//        return foodRepository.findById(foodId)
//                .map(food -> {
//                    foodRepository.delete(food);
//                    return "Delete Successfully";
//                }).orElseThrow(() -> new NotFoundException("Food item not found with Food ID: " + foodId));
//    }

}
