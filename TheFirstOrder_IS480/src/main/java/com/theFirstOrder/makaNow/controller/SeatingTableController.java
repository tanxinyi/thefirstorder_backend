package com.theFirstOrder.makaNow.controller;


import com.theFirstOrder.makaNow.model.*;
import com.theFirstOrder.makaNow.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value="/api/table/registerTable", params="qrCode", method = RequestMethod.GET)
    public SeatingTable registerTable(@RequestParam("qrCode") String qrCode){
        //Receives Qr Code and check against database, if Qr Code matches, register table and create empty order
        SeatingTable seatingTable = seatingTableService.getSeatingTableByQrCode(qrCode);

        if (seatingTable == null){
            System.out.print("QR Code Scan Error. Please Try Again!");
            return null;
        }

        return seatingTable;

    }
}