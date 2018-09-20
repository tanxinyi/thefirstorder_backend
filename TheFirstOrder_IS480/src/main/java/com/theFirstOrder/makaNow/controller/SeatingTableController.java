package com.theFirstOrder.makaNow.controller;


import com.theFirstOrder.makaNow.model.Food;
import com.theFirstOrder.makaNow.model.MenuPrice;
import com.theFirstOrder.makaNow.model.Order;
import com.theFirstOrder.makaNow.model.SeatingTable;
import com.theFirstOrder.makaNow.service.SeatingTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class SeatingTableController {

    @Autowired
    private SeatingTableService seatingTableService;

    @RequestMapping(value="/api/table/getAllTables", method = RequestMethod.GET)
    public List<SeatingTable> getAllSeatingTables(){
        return seatingTableService.getAllSeatingTables();
    }

        @RequestMapping(value="/api/table/registerTable/{qrCode}", method = RequestMethod.GET)
    public String registerTable(@PathVariable String qrCode){
        //Receives Qr Code and check against database, if Qr Code matches, register table and create empty order
        boolean registered = false;
        String orderId = "";
        Order order = null;
        SeatingTable seatingTable = seatingTableService.getSeatingTableByQrCode(qrCode);
        if (seatingTable != null){
            //Convert optional type to Seating Table
            String restaurantId = seatingTable.getSeatingTablePK().getRestaurantId().substring(1);
            String tableId = seatingTable.getSeatingTablePK().getTableId().substring(1);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddhhmm");
            LocalDateTime now = LocalDateTime.now();
            orderId = "O" + restaurantId + tableId + dtf.format(now);
            order = new Order(orderId, "NOT SENT");
            registered = true;
        }
        return order.getOrderId();
    }
}
