package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
public class Menu {

    @Id
    @JsonView(View.Public.class)
    private String menuId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_Id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    @JsonView(View.Public.class)
    private Date dateOfCreation;

}
