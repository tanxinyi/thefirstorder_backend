package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.SeatingTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatingTableRepository extends CrudRepository<SeatingTable, String> {
}
