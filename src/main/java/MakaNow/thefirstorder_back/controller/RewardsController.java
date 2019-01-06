package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Restaurant;
import MakaNow.thefirstorder_back.model.Rewards;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.RewardsRepository;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RewardsController {

    Logger logger = LoggerFactory.getLogger(RewardsController.class);

    @Autowired
    private RewardsRepository rewardsRepository;

    @Autowired
    private RestaurantController restaurantController;

    @GetMapping("/rewards")
    @JsonView(View.RewardsView.class)
    public List<Rewards> getAllRewards(){ return (List<Rewards>)rewardsRepository.findAll();}

    @GetMapping("/reward/{rewardId}")
    @JsonView(View.RewardsView.class)
    public Rewards getRewardsById(@PathVariable String rewardId) throws NotFoundException {
        Optional<Rewards> rewards = rewardsRepository.findById(rewardId);
        if(rewards.isPresent()){
            return rewards.get();
        }
        throw new NotFoundException("Rewards ID " + rewardId + " does not exist.");
    }

    @GetMapping("/rewards/restaurant/{restaurantId}")
    @JsonView(View.RewardsView.class)
    public List<Rewards> getRewardsByRestaurantId(@PathVariable String restaurantId) throws NotFoundException {
        logger.info("Getting Rewards by Restaurant Id");
        Restaurant restaurant = restaurantController.getRestaurantById(restaurantId);
        return restaurant.getRewardsList();
    }

}
