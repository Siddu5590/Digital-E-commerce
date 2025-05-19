package in.siddu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.siddu.entity.CartItem;
import in.siddu.entity.Product;
import in.siddu.entity.User;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    long countByUser(User user);
}
