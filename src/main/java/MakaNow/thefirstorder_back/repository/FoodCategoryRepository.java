package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.Food;
import MakaNow.thefirstorder_back.model.FoodCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodCategoryRepository extends CrudRepository<FoodCategory, String> {
    @Query(value = "SELECT * FROM FOOD_CATEGORY WHERE FOOD_CATEGORY_ID IN (SELECT FOOD_CATEGORY_ID FROM FOOD_PRICE WHERE MENU_ID = 'M010')",
            nativeQuery = true)
    List<FoodCategory> findFoodCategoriesByMenu(
            @Param("menuId") String menuId);
}
