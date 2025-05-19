package in.siddu.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.siddu.entity.CartItem;
import in.siddu.entity.Product;
import in.siddu.entity.User;
import in.siddu.repository.CartItemRepository;
import in.siddu.service.CartService;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private CartItemRepository crepo;
	@Override
	public List<CartItem> getAllItems(User user) {
		// TODO Auto-generated method stub
		return crepo.findByUser(user);
	}
	@Override
	public Optional<CartItem> findByUserAndProduct(User user, Product product) {
		// TODO Auto-generated method stub
		return crepo.findByUserAndProduct(user, product);
	}
	@Override
	public long countByUser(User user) {
		// TODO Auto-generated method stub
		return crepo.countByUser(user);
	}
	@Override
	public void removeCart(User u) {
		crepo.deleteAll();
		
	}

}
