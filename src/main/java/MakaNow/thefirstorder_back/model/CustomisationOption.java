package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class CustomisationOption {
    @Id
    @JsonView(View.MainView.class)
    private String customisationOptionId;

    @JsonView(View.MainView.class)
    @Column(name="customisation_id")
    private String customisationId;

    @JsonView(View.MainView.class)
    private String optionDescription;

    @JsonView(View.MainView.class)
    private double optionPrice;

    @ManyToOne
    @JoinColumn(name="customisation_id", referencedColumnName = "customisation_id", insertable = false, updatable = false)
    @JsonView(View.CustomisationOptionView.class)
    private Customisation customisation;

}
