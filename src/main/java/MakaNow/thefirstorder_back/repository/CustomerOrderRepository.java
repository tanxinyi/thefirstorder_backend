package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.CustomerOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, String> {
}
