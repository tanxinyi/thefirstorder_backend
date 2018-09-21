package com.theFirstOrder.makaNow.model;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "MenuPrice")
@Table(name = "menu_price")
@Data
public class MenuPrice {

    @EmbeddedId
    private MenuPricePK menuPricePK;
    private double price;
    private boolean outOfStock;
}
