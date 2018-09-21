package com.theFirstOrder.makaNow.repository;

import com.theFirstOrder.makaNow.model.Customisation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomisationRepository extends CrudRepository<Customisation, String> {
}