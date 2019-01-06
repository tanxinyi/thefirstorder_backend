package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.FoodCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodCategoryRepository extends CrudRepository<FoodCategory, String> {

}
