package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ManagerAllocationPK implements Serializable {

    @Column(name = "restaurant_id")
    @JsonView(View.MainView.class)
    private String restaurantId;

    @Column(name = "manager_id")
    @JsonView(View.MainView.class)
    private String managerId;

    public ManagerAllocationPK(){}

    public ManagerAllocationPK(String restaurantId, String managerId){
        this.restaurantId = restaurantId;
        this.managerId = managerId;
    }

    public String getRestaurantId(){
        return restaurantId;
    }

    public String getManagerId(){
        return managerId;
    }

}

