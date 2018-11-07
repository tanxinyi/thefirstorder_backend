package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.FoodPrice;
import MakaNow.thefirstorder_back.model.MenuFoodId;
import MakaNow.thefirstorder_back.repository.FoodPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FoodPriceService {

    @Autowired
    private FoodPriceRepository foodPriceRepository;

    public FoodPrice getFoodPriceByMenuIdAndFoodId(String menuId, String foodId){
        MenuFoodId foodPricePK = new MenuFoodId(menuId, foodId);
        Optional<FoodPrice> optionalFoodPrice = foodPriceRepository.findById(foodPricePK);
        if(optionalFoodPrice.isPresent()){
            FoodPrice food = optionalFoodPrice.get();
            return food;
        }else{
            return null;
        }
    }
}

