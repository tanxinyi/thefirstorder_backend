package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.FoodTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodTagRepository extends CrudRepository<FoodTag, String> {
}
