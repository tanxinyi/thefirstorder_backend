package MakaNow.thefirstorder_back.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class CustomisationOptionId implements Serializable {
    @Column(name = "customisation_option_id")
    private String customisationOptionId;;
    @Column(name = "food_customisation_customisation_id", insertable = false, updatable = false)
    private String foodCustomisationCustomOptionId;
}
