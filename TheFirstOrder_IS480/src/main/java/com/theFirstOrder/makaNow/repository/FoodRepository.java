package com.theFirstOrder.makaNow.repository;

import com.theFirstOrder.makaNow.model.Food;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends CrudRepository<Food, String> {
}
