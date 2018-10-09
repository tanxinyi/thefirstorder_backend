package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.SeatingTable;
import MakaNow.thefirstorder_back.repository.RestaurantRepository;
import MakaNow.thefirstorder_back.repository.SeatingTableRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class SeatingTableController {

    @Autowired
    private SeatingTableRepository seatingTableRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/seatingTables")
    public List<SeatingTable> getAllRestaurants(){
        return (List<SeatingTable>) seatingTableRepository.findAll();
    }

    @GetMapping("/seatingTables/{seatingTableId}")
    public SeatingTable getSeatingTableBySeatingTableId( @PathVariable String seatingTableId ) throws NotFoundException {
        Optional<SeatingTable> optionalSeatingTable = seatingTableRepository.findById(seatingTableId);
        if(optionalSeatingTable.isPresent()){
            return optionalSeatingTable.get();
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
                    seatingTable.setCapacity(seatingTableUpdated.getCapacity());
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
}
