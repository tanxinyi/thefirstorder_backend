package com.theFirstOrder.makaNow.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MenuPK implements Serializable {

    @Column(name = "menu_id")
    private String menuId;

    @Column(name = "restaurant_id")
    private String restaurantId;

    public MenuPK(){}

    public MenuPK(String menuId, String restaurantId){
        this.menuId = menuId;
        this.restaurantId = restaurantId;
    }

    public String getMenuId(){
        return menuId;
    }

    public String getRestaurantId(){
        return restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuPK)) return false;
        MenuPK that = (MenuPK) o;
        return Objects.equals(getMenuId(), that.getMenuId()) &&
                Objects.equals(getRestaurantId(), that.getRestaurantId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMenuId(), getRestaurantId());
    }
}
