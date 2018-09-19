package com.theFirstOrder.makaNow.repository;

import com.theFirstOrder.makaNow.model.FoodItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodItemRepository extends CrudRepository<FoodItem, String> {
}
