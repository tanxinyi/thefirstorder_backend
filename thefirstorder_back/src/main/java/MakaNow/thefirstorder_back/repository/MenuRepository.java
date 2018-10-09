package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.Menu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends CrudRepository<Menu, String> {
}
