package com.theFirstOrder.makaNow.repository;

import com.theFirstOrder.makaNow.model.SeatingTable;
import com.theFirstOrder.makaNow.model.SeatingTablePK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatingTableRepository extends CrudRepository<SeatingTable, SeatingTablePK> {
}
