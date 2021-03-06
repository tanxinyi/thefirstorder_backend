package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.FoodPrice;
import MakaNow.thefirstorder_back.model.MenuFoodCatId;
import MakaNow.thefirstorder_back.model.UpdatedFoodPrice;
import MakaNow.thefirstorder_back.repository.FoodPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FoodPriceService {

    @Autowired
    private FoodPriceRepository foodPriceRepository;

    public FoodPrice getFoodPriceByMenuIdAndFoodId(String menuId, String foodId, String foodCategoryId){
        MenuFoodCatId foodPricePK = new MenuFoodCatId(menuId, foodId, foodCategoryId);
        Optional<FoodPrice> optionalFoodPrice = foodPriceRepository.findById(foodPricePK);
        if(optionalFoodPrice.isPresent()){
            FoodPrice food = optionalFoodPrice.get();
            return food;
        }else{
            return null;
        }
    }

    public List<UpdatedFoodPrice> convertFoodPrices(List<FoodPrice> foodPrices){
        List<UpdatedFoodPrice> output = new ArrayList<>();
        for(FoodPrice foodPrice: foodPrices){
            output.add(new UpdatedFoodPrice(foodPrice));
        }
        return output;
    }
}

