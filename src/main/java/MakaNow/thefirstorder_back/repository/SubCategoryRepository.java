package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.SubCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends CrudRepository<SubCategory, String> {
}
