package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.FoodPrice;
import MakaNow.thefirstorder_back.model.MenuFoodCatId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodPriceRepository extends CrudRepository<FoodPrice, MenuFoodCatId> {
    @Query(value = "SELECT * FROM FOOD_PRICE WHERE MENU_ID = :menuId and (FOOD_CATEGORY_ID = :catId or SUB_CATEGORY_ID = :catId)",
            nativeQuery = true)
    List<FoodPrice> findFoodPricesByMenuCat(
            @Param("menuId") String menuId, @Param("catId") String catId);
}
