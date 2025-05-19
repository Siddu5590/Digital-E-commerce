package in.siddu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.siddu.entity.Order;
import jakarta.transaction.Transactional;

@Transactional
public interface OrderRepo extends JpaRepository<Order, Long>{

	
	Page<Order> findByUserId(Integer userId, Pageable pageable);

	@Modifying
	@Query("UPDATE Order SET status = 'canceled' WHERE id = :id")
	void cancelOrder(Long id);

	@Modifying
	@Query("UPDATE Order SET status = 'packing' WHERE id = :id")
	void orderPacking(Long id);

	@Modifying
	@Query("UPDATE Order SET status = 'out-for-delivery' WHERE id = :id")
	void outForDelivery(Long id);

	@Modifying
	@Query("UPDATE Order SET status = 'delivered' WHERE id = :id")
	void orderDelivered(Long id);

}
