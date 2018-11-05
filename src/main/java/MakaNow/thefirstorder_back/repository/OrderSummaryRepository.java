package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.OrderSummary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSummaryRepository extends CrudRepository<OrderSummary, String> {
}
