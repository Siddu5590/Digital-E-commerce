package in.siddu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.siddu.entity.OrderReview;

public interface ReviewRepo extends JpaRepository<OrderReview, Long> {
    boolean existsByOrderId(Long orderId);

}
