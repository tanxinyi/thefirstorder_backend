package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
public class Menu implements Comparable<Menu> {

    @Id
    @JsonView(View.MainView.class)
    private String menuId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_Id", nullable = false)
    @JsonView(View.ViewA.class)
    private Restaurant restaurant;

    @JsonView(View.MainView.class)
    private Date dateOfCreation;

    public int compareTo(Menu another){
        return -1 * this.dateOfCreation.compareTo(another.getDateOfCreation());
    }
}