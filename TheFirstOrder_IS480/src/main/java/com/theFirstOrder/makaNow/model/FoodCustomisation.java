package com.theFirstOrder.makaNow.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "FoodCustomisation")
@Table(name = "food_customisation")
@Data
public class FoodCustomisation {

    @EmbeddedId
    private FoodCustomisationPK foodCustomisationPK;
    @Column(name = "customisation_name")
    private String customisationName;
    @Column(name = "customisation_type")
    private String customisationType;
}
