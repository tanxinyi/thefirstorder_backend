package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.CustomisationOption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomisationOptionRepository extends CrudRepository<CustomisationOption, String> {
}
