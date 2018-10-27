package MakaNow.thefirstorder_back.controller;

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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FoodPriceController {

    @Autowired
    private FoodPriceRepository foodPriceRepository;
}
