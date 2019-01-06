package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.FoodPrice;
import MakaNow.thefirstorder_back.model.MenuFoodCatId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodPriceRepository extends CrudRepository<FoodPrice, MenuFoodCatId> {
}
