package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.Food;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends CrudRepository<Food, String> {
}
