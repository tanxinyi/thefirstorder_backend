package com.theFirstOrder.makaNow.service;

import com.theFirstOrder.makaNow.model.SeatingTable;
import com.theFirstOrder.makaNow.model.SeatingTablePK;
import com.theFirstOrder.makaNow.repository.SeatingTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.util.*;

@Service
public class SeatingTableService {

    @Autowired
    private SeatingTableRepository seatingTableRepository;

    public List<SeatingTable> getAllSeatingTables(){
        List<SeatingTable> seatingTables = new ArrayList<>();
        seatingTableRepository.findAll()
                .forEach(seatingTables::add);
        return seatingTables;
    }

    public SeatingTable getSeatingTableByQrCode(String qrCode){
        List<SeatingTable> seatingTables = getAllSeatingTables();
        for (int i = 0; i < seatingTables.size(); i++){
            SeatingTable seatingTable = seatingTables.get(i);
            if (seatingTable.getQrCode().equals(qrCode)){
                return seatingTable;
            }
        }
        return null;
    }

    public String getRestaurantIdByQrCode(String qrCode){
        List<SeatingTable> seatingTables = getAllSeatingTables();
        for (int i = 0; i < seatingTables.size(); i++){
            SeatingTable seatingTable = seatingTables.get(i);
            if (seatingTable.getQrCode().equals(qrCode)){
                return seatingTable.getSeatingTablePK().getRestaurantId();
            }
        }
        return null;
    }

    public void addSeatingTable(SeatingTable seatingTable){
        seatingTableRepository.save(seatingTable);
    }

    public void updateSeatingTable(String qrCode, SeatingTable seatingTable){
        seatingTableRepository.save(seatingTable);
    }

    public void deleteSeatingTable( SeatingTablePK seatingTablePK){

        seatingTableRepository.deleteById(seatingTablePK);
    }
}