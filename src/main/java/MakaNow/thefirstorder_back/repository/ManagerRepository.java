package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.Manager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends CrudRepository<Manager, String> {
}