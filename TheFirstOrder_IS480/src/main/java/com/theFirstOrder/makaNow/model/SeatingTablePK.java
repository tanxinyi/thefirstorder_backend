package com.theFirstOrder.makaNow.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SeatingTablePK implements Serializable {

    @Column(name = "table_id")
    private String tableId;

    @Column(name = "restaurant_id")
    private String restaurantId;

    public SeatingTablePK(){}

    public SeatingTablePK(String tableId, String restaurantId){
        this.tableId = tableId;
        this.restaurantId = restaurantId;
    }

    public String getTableId(){
        return tableId;
    }

    public String getRestaurantId(){
        return restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeatingTablePK)) return false;
        SeatingTablePK that = (SeatingTablePK) o;
        return Objects.equals(getTableId(), that.getTableId()) &&
                Objects.equals(getRestaurantId(), that.getRestaurantId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTableId(), getRestaurantId());
    }
}
