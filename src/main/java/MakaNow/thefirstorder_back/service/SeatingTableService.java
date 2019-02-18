package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.SeatingTable;
import MakaNow.thefirstorder_back.repository.SeatingTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SeatingTableService {

    @Autowired
    private SeatingTableRepository seatingTableRepository;

    //For all other logics
    public String getNewTableId() {
        List<String> tableIds = new ArrayList<>();
        List<SeatingTable> seatingTables = (List<SeatingTable>) seatingTableRepository.findAll();

        //For first menu created to avoid index out of bounds error
        if (seatingTables.size() == 0) {
            return "T001";
        }

        for (int i = 0; i < seatingTables.size(); i++) {
            SeatingTable seatingTable = seatingTables.get(i);
            String tableId = seatingTable.getQrCode();
            tableIds.add(tableId);
        }
        Collections.sort(tableIds);
        String lastTableId = tableIds.get(tableIds.size() - 1);
        int tableIdNumber = Integer.parseInt(lastTableId.substring(1));
        int newTableIdNumber = tableIdNumber + 1;
        int length = String.valueOf(newTableIdNumber).length();

        if (length == 1) {
            return ("T00" + newTableIdNumber);
        } else if (length == 2) {
            return ("T0" + newTableIdNumber);
        }
        return ("T" + newTableIdNumber);
    }
}
