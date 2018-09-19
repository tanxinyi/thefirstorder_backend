package com.theFirstOrder.makaNow.model;

import lombok.Data;
import org.hibernate.criterion.Order;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;

@Entity(name = "SeatingTable")
@Table(name = "seating_table")
@Data
public class SeatingTable {

    @EmbeddedId
    private SeatingTablePK seatingTablePK;
    private int capacity;
    private String qrCode;
}
