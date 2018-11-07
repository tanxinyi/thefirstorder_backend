package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.Orders;
import MakaNow.thefirstorder_back.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    public Iterable<Orders> save(List<Orders> orders){
        return ordersRepository.saveAll(orders);
    }

    public Orders save(Orders orders){
        return ordersRepository.save(orders);
    }

    public Iterable<Orders> list(){
        return ordersRepository.findAll();
    }
}
