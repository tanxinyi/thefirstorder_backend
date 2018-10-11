package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.FoodCustomisation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodCustomisationRepository extends CrudRepository<FoodCustomisation, String> {
}
