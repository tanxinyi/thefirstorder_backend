package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.Customisation;
import MakaNow.thefirstorder_back.model.CustomisationOption;
import MakaNow.thefirstorder_back.model.FoodCategory;
import MakaNow.thefirstorder_back.model.FoodPrice;
import MakaNow.thefirstorder_back.repository.CustomisationOptionRepository;
import MakaNow.thefirstorder_back.repository.CustomisationRepository;
import MakaNow.thefirstorder_back.repository.FoodCategoryRepository;
import MakaNow.thefirstorder_back.repository.FoodPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomisationService {

    @Autowired
    private CustomisationRepository customisationRepository;

    public String getNewCustomisationId(){
        List<String> customisationIds = new ArrayList<>();
        List<Customisation> customisations = (List<Customisation>) customisationRepository.findAll();

        //For first food created to avoid index out of bounds error
        if(customisations.size() == 0){
            return "C001";
        }

        for(int i = 0; i < customisations.size(); i++){
            Customisation customisation = customisations.get(i);
            String customisationId = customisation.getCustomisationId();
            customisationIds.add(customisationId);
        }
        Collections.sort(customisationIds);
        String lastCustomisationId = customisationIds.get(customisationIds.size()-1);
        int customisationIdNumber = Integer.parseInt(lastCustomisationId.substring(1));
        int newCustomisationIdNumber = customisationIdNumber + 1;
        int length = String.valueOf(newCustomisationIdNumber).length();

        if(length == 1) {
            return ("C00" + newCustomisationIdNumber);
        }
        else if(length == 2){
            return ("C0" + newCustomisationIdNumber);
        }
        return ("C" + newCustomisationIdNumber);
    }
}
