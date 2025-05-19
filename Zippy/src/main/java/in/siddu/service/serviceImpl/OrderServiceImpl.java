package in.siddu.service.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.siddu.entity.CartItem;
import in.siddu.entity.Order;
import in.siddu.entity.User;
import in.siddu.repository.OrderRepo;
import in.siddu.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Override
    public List<Order> placeOrder(User user, List<CartItem> cartItems, String deliveryAddress, String paymentMode) {
        List<Order> placedOrders = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Order order = new Order();
            order.setUser(user);
            order.setProduct(cartItem.getProduct());
            order.setQuantity(cartItem.getQuantity());
            order.setTotalPrice(cartItem.getTotalPrice());
            order.setDeliveryAddress(deliveryAddress);
            order.setPaymentMode(paymentMode);
            order.setOrderDate(LocalDate.now());

            placedOrders.add(orderRepo.save(order));
        }

        return placedOrders;
    }


	@Override
	public Page<Order> getUserOrders(Integer id, int page, int size) {
		 Pageable pageable = PageRequest.of(page, size);
	        return orderRepo.findByUserId(id, pageable);
	}
	
	@Override
	public Page<Order> getAllOrders(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return orderRepo.findAll(pageable);
	}

	@Override
	public Optional<Order> findOrder(Long id) {
		
		return Optional.ofNullable(orderRepo.findById(id).get());
	}



	@Override
	public void cancelOrder(Long id) {
		// TODO Auto-generated method stub
		orderRepo.cancelOrder(id);
	}

	@Override
	public void orderPacking(Long id) {
		
		orderRepo.orderPacking(id);
		
	}

	@Override
	public void outForDelivery(Long id) {
		
		orderRepo.outForDelivery(id);
		
	}

	@Override
	public void orderDelivered(Long id) {
		
		orderRepo.orderDelivered(id);
		
	}
	
		

}
