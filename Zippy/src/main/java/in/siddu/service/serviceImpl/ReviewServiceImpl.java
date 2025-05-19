package in.siddu.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.siddu.entity.Order;
import in.siddu.entity.OrderReview;
import in.siddu.repository.OrderRepo;
import in.siddu.repository.ReviewRepo;
import in.siddu.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService{

	@Autowired
    private OrderRepo orderRepository;

    @Autowired
    private ReviewRepo reviewRepo;

    public boolean submitReview(Long orderId, int rating, String message) {
        if (reviewRepo.existsByOrderId(orderId)) return false;

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || !"delivered".equals(order.getStatus())) return false;

        OrderReview review = new OrderReview();
        review.setOrder(order);
        review.setRating(rating);
        review.setMessage(message);

        reviewRepo.save(review);
        return true;
    }

	@Override
	public List<OrderReview> getOrderReview() {
		
		return reviewRepo.findAll();
	}

	
}
