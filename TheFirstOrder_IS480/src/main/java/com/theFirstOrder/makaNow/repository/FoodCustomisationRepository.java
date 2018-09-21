package com.theFirstOrder.makaNow.repository;

import com.theFirstOrder.makaNow.model.FoodCustomisation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodCustomisationRepository extends CrudRepository<FoodCustomisation, FoodCustomisationRepository> {
}
