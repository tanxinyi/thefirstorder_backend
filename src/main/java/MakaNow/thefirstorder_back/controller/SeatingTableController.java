package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.RestaurantRepository;
import MakaNow.thefirstorder_back.repository.SeatingTableRepository;
import MakaNow.thefirstorder_back.service.SeatingTableService;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class SeatingTableController {

    Logger logger = LoggerFactory.getLogger(SeatingTableController.class);

    @Autowired
    private SeatingTableRepository seatingTableRepository;

    @Autowired
    private SeatingTableService seatingTableService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/seatingTables")
    public List<SeatingTable> getAllRestaurants(){
        return (List<SeatingTable>) seatingTableRepository.findAll();
    }

    @GetMapping("/seatingTables/{seatingTableId}")
    @JsonView(View.SeatingTableView.class)
    public SeatingTable getSeatingTableBySeatingTableId( @PathVariable String seatingTableId ) throws NotFoundException {
        logger.info("Getting Seating Table by QR Code");
        Optional<SeatingTable> optionalSeatingTable = seatingTableRepository.findById(seatingTableId);
        if(optionalSeatingTable.isPresent()){
            return optionalSeatingTable.get();
        }else{
            throw new NotFoundException("Seating Table ID " + seatingTableId + " does not exist");
        }
    }

    @GetMapping("/seatingTables/register/{seatingTableId}")
    @JsonView(View.SeatingTableView.class)
    public UpdatedSeatingTable registerSeatingTable(@PathVariable String seatingTableId ) throws NotFoundException {
        logger.info("Getting Seating Table by QR Code");
        Optional<SeatingTable> optionalSeatingTable = seatingTableRepository.findById(seatingTableId);
        if(optionalSeatingTable.isPresent()){
            return new UpdatedSeatingTable(optionalSeatingTable.get());
        }else{
            throw new NotFoundException("Seating Table ID " + seatingTableId + " does not exist");
        }
    }

    @PostMapping("/restaurants/{restaurantId}/seatingTables")
    public SeatingTable addSeatingTable(@PathVariable String restaurantId,
                                        @Valid @RequestBody SeatingTable seatingTable) throws NotFoundException {
        return restaurantRepository.findById(restaurantId)
                .map(restaurant -> {
                    seatingTable.setRestaurant(restaurant);
                    return seatingTableRepository.save(seatingTable);
                }).orElseThrow(()-> new NotFoundException("Restaurant Not Found."));

    }

    @PutMapping("/restaurants/{restaurantId}/seatingTables/{seatingTableId}")
    public SeatingTable updateSeatingTable(@PathVariable String restaurantId,
                                           @PathVariable String seatingTableId,
                                           @Valid @RequestBody SeatingTable seatingTableUpdated) throws NotFoundException {
        if(!restaurantRepository.existsById(restaurantId)){
            throw new NotFoundException("Restaurant Not Found.");
        }

        return seatingTableRepository.findById(seatingTableId)
                .map(seatingTable -> {
                    seatingTable.setTableCapacity(seatingTableUpdated.getTableCapacity());
                    return seatingTableRepository.save(seatingTable);
                }).orElseThrow(()-> new NotFoundException("Update Unsuccessful."));
    }

    @DeleteMapping("/restaurants/{restaurantId}/seatingTables/{seatingTableId}")
    public String deleteSeatingTable(@PathVariable String restaurantId, @PathVariable String seatingTableId) throws NotFoundException {
        if(!restaurantRepository.existsById(restaurantId)){
            throw new NotFoundException("Restaurant Not Found.");
        }

        return seatingTableRepository.findById(seatingTableId)
                .map(seatingTable -> {
                    seatingTableRepository.delete(seatingTable);
                    return "Delete Successfully.";
                }).orElseThrow(()-> new NotFoundException("Delete Unsuccessful"));
    }

    @GetMapping("/seatingTables/newTableId")
    public String getNewTableId(){
        return seatingTableService.getNewTableId();
    }
}
