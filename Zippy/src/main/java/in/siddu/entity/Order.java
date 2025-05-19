package in.siddu.entity;

import java.sql.Date;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private Double totalPrice;

    private String deliveryAddress;

    private String paymentMode;
    
    private LocalDate orderDate;
    
    private String status = "Pending";
    
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderReview review;


}
