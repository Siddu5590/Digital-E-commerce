package in.siddu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.siddu.entity.CartItem;
import in.siddu.entity.Order;
import in.siddu.entity.OrderReview;
import in.siddu.entity.User;
import in.siddu.service.CartService;
import in.siddu.service.OrderService;
import in.siddu.service.ReviewService;
import in.siddu.service.serviceImpl.EmailService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/place")
    public String placeOrder(@RequestParam("deliveryAddress") String deliveryAddress,
                             @RequestParam("paymentMode") String paymentMode,
                             HttpSession session, RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        List<CartItem> cartItems = cartService.getAllItems(user);

        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty.");
            return "redirect:/cart";
        }

        try {
            // Step 1: Place Orders
            List<Order> orders = orderService.placeOrder(user, cartItems, deliveryAddress, paymentMode);

            // Step 2: Clear Cart
            cartService.removeCart(user);

            // Step 3: Send Email with PDF
            emailService.sendEmail(user, orders);

            redirectAttributes.addFlashAttribute("success", "Order placed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to place the order. Please try again.");
        }

        return "redirect:/cart";
    }

    
    @GetMapping("/orders")
    public String orders(
            @RequestParam(defaultValue = "0") int page, 
            HttpSession session, 
            Model m) {
        
        User u = (User) session.getAttribute("user");
        if (u == null) {
            return "redirect:/user/login";
        }

        Page<Order> orderPage = orderService.getUserOrders(u.getId(), page, 10);

        m.addAttribute("orders", orderPage.getContent());
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", orderPage.getTotalPages());

        return "orders";
    }
    
    @GetMapping("/allOrders")
    public String allOrders(
            @RequestParam(defaultValue = "0") int page, 
            HttpSession session, 
            Model m) {
        
        User u = (User) session.getAttribute("user");
        if (u == null) {
            return "redirect:/user/login";
        }

        Page<Order> orderPage = orderService.getAllOrders(page, 10);

        m.addAttribute("orders", orderPage.getContent());
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", orderPage.getTotalPages());

        return "allOrders";
    }

    @GetMapping("/cancel")
    public String cancelOrder(@RequestParam Long id, RedirectAttributes attribute) {
        Optional<Order> findOrder = orderService.findOrder(id);
        
        if (findOrder.isEmpty()) {
            attribute.addFlashAttribute("error", "Order Not found..!");
            return "redirect:/orders";
        }

        orderService.cancelOrder(id);
        
        attribute.addFlashAttribute("success", "Order Canceled Successfully..!");
        return "redirect:/order/orders";
    }
    
    @GetMapping("/admincancel")
    public String cancelOrderByAdmin(@RequestParam Long id, RedirectAttributes attribute) {
        Optional<Order> findOrder = orderService.findOrder(id);
        
        if (findOrder.isEmpty()) {
            attribute.addFlashAttribute("error", "Order Not found..!");
            return "redirect:/orders";
        }

        orderService.cancelOrder(id);
        
        attribute.addFlashAttribute("success", "Order Canceled Successfully..!");
        
        return "redirect:/order/allOrders";
    }
    
    @GetMapping("/packing")
    public String orderPacking(@RequestParam Long id, RedirectAttributes attribute) {
        Optional<Order> findOrder = orderService.findOrder(id);
        
        if (findOrder.isEmpty()) {
            attribute.addFlashAttribute("error", "Order Not found..!");
            return "redirect:/orders";
        }

        orderService.orderPacking(id);
        
        attribute.addFlashAttribute("success", "order is packing..!");
        
        return "redirect:/order/allOrders";
    }
    
    @GetMapping("/outfordelivery")
    public String outForDelivery(@RequestParam Long id, RedirectAttributes attribute) {
        Optional<Order> findOrder = orderService.findOrder(id);
        
        if (findOrder.isEmpty()) {
            attribute.addFlashAttribute("error", "Order Not found..!");
            return "redirect:/orders";
        }

        orderService.outForDelivery(id);
        
        attribute.addFlashAttribute("success", "Order Out for delivery..!");
        
        return "redirect:/order/allOrders";
    }
    
    @GetMapping("/delivered")
    public String orderDelivered(@RequestParam Long id, RedirectAttributes attribute) {
        Optional<Order> findOrder = orderService.findOrder(id);
        
        if (findOrder.isEmpty()) {
            attribute.addFlashAttribute("error", "Order Not found..!");
            return "redirect:/orders";
        }

        orderService.orderDelivered(id);
        
        attribute.addFlashAttribute("success", "Order Delivered Successfully..!");
        
        return "redirect:/order/allOrders";
    }

    @PostMapping("/submitReview")
    @ResponseBody
    public Map<String, Object> submitReview(@RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long orderId = Long.valueOf(data.get("orderId").toString());
            int rating = (int) data.get("rating");
            String message = data.get("reviewMessage").toString();

            boolean success = reviewService.submitReview(orderId, rating, message);
            response.put("success", success);
            response.put("message", success ? "Review saved." : "Review already submitted or invalid order.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error occurred.");
        }
        return response;
    }

    @GetMapping("/viewReview")
    public String viewReview( RedirectAttributes attribute, Model model)
    {
        
        List<OrderReview> review=reviewService.getOrderReview();
        if(review.isEmpty())
        {
        	attribute.addFlashAttribute("noReviewId","no reviews found");
        }
        else {
        	model.addAttribute("review",review);
        }
    	
    	return "viewReviews";
    }
    
}
