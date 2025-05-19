package in.siddu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import in.siddu.entity.CartItem;
import in.siddu.entity.Order;
import in.siddu.entity.User;

public interface OrderService {

	List<Order> placeOrder(User user, List<CartItem> cartItems, String deliveryAddress, String paymentMode);

	Page<Order> getUserOrders(Integer id, int page, int size);

	Optional<Order> findOrder(Long id);

	void cancelOrder(Long id);

	Page<Order> getAllOrders(int page, int i);

	void orderPacking(Long id);

	void outForDelivery(Long id);

	void orderDelivered(Long id);
	
}
