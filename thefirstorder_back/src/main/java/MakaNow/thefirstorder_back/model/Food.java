package MakaNow.thefirstorder_back.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Food {

    @Id
    private String foodId;
    private String name;
    private String description;
    private String category;
}
