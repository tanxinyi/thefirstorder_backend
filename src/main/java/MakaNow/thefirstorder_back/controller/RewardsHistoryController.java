//package MakaNow.thefirstorder_back.controller;
//
//import MakaNow.thefirstorder_back.model.Customer;
//import MakaNow.thefirstorder_back.model.RewardsHistory;
//import MakaNow.thefirstorder_back.model.View;
//import MakaNow.thefirstorder_back.repository.RewardsHistoryRepository;
//import com.fasterxml.jackson.annotation.JsonView;
//import javassist.NotFoundException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api")
//public class RewardsHistoryController {
//
//    Logger logger = LoggerFactory.getLogger(RewardsHistoryController.class);
//
//    @Autowired
//    private RewardsController rewardsController;
//
//    @Autowired
//    private RewardsHistoryRepository rewardsHistoryRepository;
//
//    @Autowired
//    private CustomerController customerController;
//
//    @GetMapping("/rewardsHistories")
//    @JsonView(View.RewardsHistoryView.class)
//    public List<RewardsHistory> getAllRewardsHistory(){
//        return (List<RewardsHistory>)rewardsHistoryRepository.findAll();
//    }
//
//    @GetMapping("/rewardsHistory/{rewardHistoryId}")
//    @JsonView(View.RewardsHistoryView.class)
//    public RewardsHistory getRewardHistoryById(@PathVariable String rewardHistoryId) throws NotFoundException {
//        Optional<RewardsHistory> rewardsHistory = rewardsHistoryRepository.findById(rewardHistoryId);
//        if(rewardsHistory.isPresent()){
//            return rewardsHistory.get();
//        }
//        throw new NotFoundException("Reward History Id " + rewardHistoryId + " does not exist");
//    }
//
//    @GetMapping("/rewardsHistory/customer/{customerId}")
//    @JsonView(View.RewardsHistoryView.class)
//    public List<RewardsHistory> getRewardsHistoryByCustomerId(@PathVariable String customerId) throws NotFoundException{
//        logger.info("Getting Rewards History List by Customer Id");
//        Customer customer = customerController.getCustomerById(customerId);
//        return customer.getRewardsHistoryList();
//    }
//
//}
