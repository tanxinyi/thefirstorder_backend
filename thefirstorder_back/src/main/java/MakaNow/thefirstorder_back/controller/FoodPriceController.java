package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.FoodCustomisation;
import MakaNow.thefirstorder_back.model.FoodPrice;
import MakaNow.thefirstorder_back.model.MenuFoodId;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.FoodPriceRepository;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FoodPriceController {

    @Autowired
    private FoodPriceRepository foodPriceRepository;

    @GetMapping("/food_price/{menuId}/{foodId}/customisation")
    @JsonView(View.MainView.class)
    public List<FoodCustomisation> getCustomisation(@PathVariable String menuId, @PathVariable String foodId) throws NotFoundException{

        Optional<FoodPrice>  optionalFoodPrice = foodPriceRepository.findById(new MenuFoodId(menuId, foodId));
        if(!optionalFoodPrice.isPresent()) throw new NotFoundException("MenuId("+menuId+") and FoodId("+foodId+") not valid");
        FoodPrice foodPrice = optionalFoodPrice.get();

        return foodPrice.getFoodCustomisations();

    }
}
