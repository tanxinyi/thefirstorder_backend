package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.Admin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<Admin, String> {
}
