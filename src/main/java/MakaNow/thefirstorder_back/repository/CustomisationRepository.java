package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.Customisation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomisationRepository extends CrudRepository<Customisation, String> {
}
