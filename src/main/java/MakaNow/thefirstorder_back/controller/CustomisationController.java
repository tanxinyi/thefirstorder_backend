package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Customisation;
import MakaNow.thefirstorder_back.model.FoodPrice;
import MakaNow.thefirstorder_back.model.MenuFoodCatId;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.CustomisationRepository;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class CustomisationController {

    Logger logger = LoggerFactory.getLogger(CustomisationController.class);

    @Autowired
    private CustomisationRepository customisationRepository;

    @Autowired
    private FoodPriceController foodPriceController;

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
}
