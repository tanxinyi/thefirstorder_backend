package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.Customer;
        import org.springframework.data.annotation.CreatedBy;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {
}
