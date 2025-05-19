package in.siddu.service;

import java.util.List;
import java.util.Optional;

import in.siddu.entity.CartItem;
import in.siddu.entity.Product;
import in.siddu.entity.User;

public interface CartService {

	List<CartItem> getAllItems(User  user);
	
	Optional<CartItem> findByUserAndProduct(User user, Product product);
    long countByUser(User user);
    
    public void removeCart(User u);
}
