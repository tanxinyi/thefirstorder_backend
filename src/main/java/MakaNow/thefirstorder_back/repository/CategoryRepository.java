package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, String> {
}
