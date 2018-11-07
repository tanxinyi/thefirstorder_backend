package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.Orders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends CrudRepository<Orders, String> {
}
