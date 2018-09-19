package com.theFirstOrder.makaNow.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MenuPricePK implements Serializable {

    @Column(name = "menu_id")
    private String menuId;

    @Column(name = "food_id")
    private String foodId;

    public MenuPricePK(){}

    public MenuPricePK(String menuId, String foodId){
        this.menuId = menuId;
        this.foodId = foodId;
    }

    public String getMenuId(){
        return menuId;
    }

    public String getFoodId(){
        return foodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuPricePK)) return false;
        MenuPricePK that = (MenuPricePK) o;
        return Objects.equals(getMenuId(), that.getMenuId()) &&
                Objects.equals(getFoodId(), that.getFoodId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMenuId(), getFoodId());
    }
}
