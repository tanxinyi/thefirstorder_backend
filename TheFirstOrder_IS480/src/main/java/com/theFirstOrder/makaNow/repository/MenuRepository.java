package com.theFirstOrder.makaNow.repository;

import com.theFirstOrder.makaNow.model.Menu;
import com.theFirstOrder.makaNow.model.MenuPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends CrudRepository<Menu, MenuPK> {
}
