package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.FoodCategoryRepository;
import MakaNow.thefirstorder_back.repository.MenuRepository;
import MakaNow.thefirstorder_back.repository.SubCategoryRepository;
import MakaNow.thefirstorder_back.service.FoodCategoryService;
import MakaNow.thefirstorder_back.service.SubCategoryService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class SubCategoryController {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private SubCategoryService subCategoryService;

    @PostMapping("/subCategories")
    @JsonView(View.SubCategoryView.class)
    public List<SubCategory> getAllSubCategories(){ return (List<SubCategory>) subCategoryRepository.findAll();
    }

    @PostMapping("/subCategories/{menuId}")
    @JsonView(View.SubCategoryView.class)
    public List<SubCategory> getSubCategoriesByMenuId(@PathVariable("menuId") String menuId){
        Menu menu = menuRepository.findById(menuId).get();
        List<FoodPrice> foodPrices = menu.getFoodPrices();
        Set<String> subCategoryIds = new HashSet<String>();

        for (int i = 0; i < foodPrices.size(); i++){
            FoodPrice foodPrice = foodPrices.get(i);
            String subCategoryId = foodPrice.getSubCategoryId();
            subCategoryIds.add(subCategoryId);
        }
        return (List<SubCategory>) subCategoryRepository.findAllById(subCategoryIds);
    }

    @PostMapping("/subCategories/getSubCategoriesByCategoryId/{categoryId}")
    @JsonView(View.SubCategoryView.class)
    public List<SubCategory> getSubCategoriesByCategoryId(@PathVariable("categoryId") String categoryId){
        List<SubCategory> toReturn = new ArrayList<>();
        List<SubCategory> subCategories = (List<SubCategory>) subCategoryRepository.findAll();
        for(int i = 0; i < subCategories.size(); i++){
            SubCategory subCategory = subCategories.get(i);
            if(subCategory.getCategoryId().equals(categoryId)){
                toReturn.add(subCategory);
            }
        }
        return toReturn;
    }

    @PostMapping("/subCategories/addSubCategory/{categoryId}")
    @JsonView(View.CategoryView.class)
    public ResponseEntity<?> addSubCategory( @PathVariable("categoryId") String categoryId, @RequestBody NewSubCategory newSubCategory){

        String subCategoryId = subCategoryService.getNewSubCategoryId();

        String subCategoryName = newSubCategory.getSubCategoryName();

        String subCategoryImage = newSubCategory.getSubCategoryImg();

        byte[] subCategoryImgByte = subCategoryImage.getBytes();

        SubCategory subCategory = new SubCategory();
        subCategory.setSubCategoryId(subCategoryId);
        subCategory.setCategoryId(categoryId);
        subCategory.setSubCategoryName(subCategoryName);
        subCategory.setSubCategoryImage(subCategoryImgByte);

        subCategoryRepository.save(subCategory);

        return new ResponseEntity( subCategoryId, HttpStatus.OK);
    }
}

@Data
class NewSubCategory{
    private String subCategoryName;
    private String subCategoryImg;
}

