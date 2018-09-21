package com.theFirstOrder.makaNow.service;

import com.theFirstOrder.makaNow.model.Customisation;
import com.theFirstOrder.makaNow.repository.CustomisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomisationService {

    @Autowired
    private CustomisationRepository customisationRepository;

    public List<Customisation> getAllCustomisations(){
        List<Customisation> customisations = new ArrayList<>();
        customisationRepository.findAll()
                .forEach(customisations::add);
        return customisations;
    }

    public List<Customisation> getCustomisationsByCustomisationId(String customisationId){
        List<Customisation> customisations = getAllCustomisations();
        List<Customisation> specificCustomisations = new ArrayList<>();

        for (int i = 0; i < customisations.size(); i++){
            Customisation customisation = customisations.get(i);
            if (customisation.getCustomisationId().equals(customisationId)){
                specificCustomisations.add(customisation);
            }
        }
        return specificCustomisations;
    }
}
