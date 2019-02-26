package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class UpdatedSeatingTable {

    private String qrCode;
    private String restaurantId;
    private int tableCapacity;
    private UpdatedRestaurant updatedRestaurant;

    public UpdatedSeatingTable(SeatingTable seatingTable){
        this.qrCode = seatingTable.getQrCode();
        this.restaurantId = seatingTable.getRestaurantId();
        this.tableCapacity = seatingTable.getTableCapacity();
        this.updatedRestaurant = new UpdatedRestaurant(seatingTable.getRestaurant());
    }

}
