package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.SubCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends CrudRepository<SubCategory, String> {

    @Query(value = "SELECT * FROM SUB_CATEGORY WHERE SUB_CATEGORY_ID IN (SELECT SUB_CATEGORY_ID FROM FOOD_PRICE WHERE MENU_ID = :menuId AND FOOD_CATEGORY_ID = :catId)",
            nativeQuery = true)
    List<SubCategory> findSubCategoriesByMenuCat(
            @Param("menuId") String menuId, @Param("catId") String catId);
}
