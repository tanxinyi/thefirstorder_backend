package com.theFirstOrder.makaNow.model;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;

@Entity(name = "Menu")
@Table(name = "menu")
@Data
public class Menu {

    @EmbeddedId
    private MenuPK menuPK;
    private String dateOfCreation;

    public Menu(MenuPK menuPK, String dateOfCreation){
        this.menuPK = menuPK;
        this.dateOfCreation = dateOfCreation;
        //HashMap<FoodItem, Double> foodAndPrice = new HashMap<>();
    }
}
