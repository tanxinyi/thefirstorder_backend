package com.theFirstOrder.makaNow.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Customisation {

    @Id
    @Column(name = "customisation_id")
    private String customisationId;
    @Column(name = "food_id")
    private String foodId;
    @Column(name="customisation_type")
    private String customisationType;
    @Column(name="customisation_name")
    private String customisationName;
    /*
    @Column(name="customisation_option_2")
    private String customisationOption2;
    @Column(name="customisation_option_3")
    private String customisationOption3;
    @Column(name="customisation_option_4")
    private String customisationOption4;
    @Column(name="customisation_option_5")
    private String customisationOption5;*/

//    public Customisation( String customisationId, String customisationDesc, String customisationType){
//        this.customisationId = customisationId;
//        this.customisationDesc = customisationDesc;
//        this.customisationType = customisationType;
//
//        List<String> customisationOptions = new ArrayList<>();
//
//        if (customisationOption1 != null){
//            customisationOptions.add(customisationOption1);
//        }
//
//        if (customisationOption2 != null){
//            customisationOptions.add(customisationOption2);
//        }
//
//        if (customisationOption3 != null){
//            customisationOptions.add(customisationOption3);
//        }
//
//        if (customisationOption4 != null){
//            customisationOptions.add(customisationOption4);
//        }
//
//        if (customisationOption5 != null){
//            customisationOptions.add(customisationOption5);
//        }
//    }
}
