package com.theFirstOrder.makaNow.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FoodCustomisationPK implements Serializable {

    @Column(name = "food_id")
    private String foodId;

    @Column(name = "customisation_id_1")
    private String customisationId1;

    public FoodCustomisationPK(){}

    public FoodCustomisationPK(String foodId, String customisationId1){
        this.foodId = foodId;
        this.customisationId1 = customisationId1;
    }

    public String getFoodId(){
        return foodId;
    }

    public String getCustomisationId1(){
        return customisationId1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodCustomisationPK)) return false;
        FoodCustomisationPK that = (FoodCustomisationPK) o;
        return Objects.equals(getFoodId(), that.getFoodId()) &&
                Objects.equals(getCustomisationId1(), that.getCustomisationId1());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFoodId(), getCustomisationId1());
    }
}
