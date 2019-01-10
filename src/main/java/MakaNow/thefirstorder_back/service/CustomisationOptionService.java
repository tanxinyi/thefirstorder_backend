package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.CustomisationOption;
import MakaNow.thefirstorder_back.model.FoodCategory;
import MakaNow.thefirstorder_back.model.FoodPrice;
import MakaNow.thefirstorder_back.repository.CustomisationOptionRepository;
import MakaNow.thefirstorder_back.repository.FoodCategoryRepository;
import MakaNow.thefirstorder_back.repository.FoodPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomisationOptionService {

    @Autowired
    private CustomisationOptionRepository customisationOptionRepository;

    public String getNewCustomisationOptionId(){
        List<String> customisationOptionIds = new ArrayList<>();
        List<CustomisationOption> customisationOptions = (List<CustomisationOption>) customisationOptionRepository.findAll();

        //For first food created to avoid index out of bounds error
        if(customisationOptions.size() == 0){
            return "CO001";
        }

        for(int i = 0; i < customisationOptions.size(); i++){
            CustomisationOption customisationOption = customisationOptions.get(i);
            String customisationOptionId = customisationOption.getCustomisationOptionId();
            customisationOptionIds.add(customisationOptionId);
        }
        Collections.sort(customisationOptionIds);
        String lastCustomisationOptionId = customisationOptionIds.get(customisationOptionIds.size()-1);
        int customisationOptionIdNumber = Integer.parseInt(lastCustomisationOptionId.substring(2));
        int newCustomisationOptionIdNumber = customisationOptionIdNumber + 1;
        int length = String.valueOf(newCustomisationOptionIdNumber).length();

        if(length == 1) {
            return ("CO00" + newCustomisationOptionIdNumber);
        }
        else if(length == 2){
            return ("CO0" + newCustomisationOptionIdNumber);
        }
        return ("CO" + newCustomisationOptionIdNumber);
    }
}
