package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.Restaurant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends CrudRepository<Restaurant, String> {
}
