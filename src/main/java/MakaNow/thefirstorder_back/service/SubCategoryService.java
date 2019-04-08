package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.FoodCategory;
import MakaNow.thefirstorder_back.model.SubCategory;
import MakaNow.thefirstorder_back.model.UpdatedSubCategory;
import MakaNow.thefirstorder_back.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    public String getNewSubCategoryId(){
        List<String> subCategoryIds = new ArrayList<>();
        List<SubCategory> subCategories = (List<SubCategory>) subCategoryRepository.findAll();

        //For first food created to avoid index out of bounds error
        if(subCategories.size() == 0){
            return "SC001";
        }

        for(int i = 0; i < subCategories.size(); i++){
            SubCategory subCategory = subCategories.get(i);
            String subCategoryId = subCategory.getSubCategoryId();
            subCategoryIds.add(subCategoryId);
        }
        Collections.sort(subCategoryIds);
        String lastFoodCategoryId = subCategoryIds.get(subCategoryIds.size()-1);
        int subCategoryIdNumber = Integer.parseInt(lastFoodCategoryId.substring(2));
        int newSubCategoryIdNumber = subCategoryIdNumber + 1;
        int length = String.valueOf(newSubCategoryIdNumber).length();

        if(length == 1) {
            return ("SC00" + newSubCategoryIdNumber);
        }
        else if(length == 2){
            return ("SC0" + newSubCategoryIdNumber);
        }
        return ("SC" + newSubCategoryIdNumber);
    }

    public List<UpdatedSubCategory> convertSubCategory(List<SubCategory> subCategories){
        List<UpdatedSubCategory> output = new ArrayList<>();
        for(SubCategory subCategory: subCategories){
            output.add(new UpdatedSubCategory(subCategory));
        }
        return output;
    }
}
