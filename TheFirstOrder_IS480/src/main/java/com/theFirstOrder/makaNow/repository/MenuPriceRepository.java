package com.theFirstOrder.makaNow.repository;

import com.theFirstOrder.makaNow.model.MenuPrice;
import com.theFirstOrder.makaNow.model.MenuPricePK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuPriceRepository extends CrudRepository<MenuPrice, MenuPricePK> {
}
