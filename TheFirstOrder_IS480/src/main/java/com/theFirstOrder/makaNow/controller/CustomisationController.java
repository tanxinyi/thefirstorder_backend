package com.theFirstOrder.makaNow.controller;

import com.theFirstOrder.makaNow.model.Customisation;
import com.theFirstOrder.makaNow.service.CustomisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomisationController {

    @Autowired
    private CustomisationService customisationService;

    @RequestMapping(value="/api/table/getAllCustomisations", method = RequestMethod.GET)
    public List<Customisation> getAllFoodItems(){
        return customisationService.getAllCustomisations();
    }
}
