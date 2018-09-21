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
    @Column(name = "customisation_id_2")
    private String customisationId2;
    @Column(name = "customisation_id_3")
    private String customisationId3;
}
