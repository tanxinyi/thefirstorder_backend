package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.ManagerAllocation;
import MakaNow.thefirstorder_back.model.ManagerAllocationPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerAllocationRepository extends CrudRepository<ManagerAllocation, ManagerAllocationPK> {
}
