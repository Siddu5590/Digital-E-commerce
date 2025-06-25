package in.siddu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    // ✅ Place Order
    @PostMapping("/place")
    public String placeOrder(@RequestParam("deliveryAddress") String deliveryAddress,
                             @RequestParam("paymentMode") String paymentMode,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/user/login";

        List<CartItem> cartItems = cartService.getAllItems(user);
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty.");
            return "redirect:/cart";
        }

        try {
            List<Order> orders = orderService.placeOrder(user, cartItems, deliveryAddress, paymentMode);
            cartService.removeCart(user);
            emailService.sendEmail(user, orders);

            redirectAttributes.addFlashAttribute("success", "Order placed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to place order. Try again.");
        }

        return "redirect:/cart";
    }

    // ✅ View User Orders (Paginated)
    @GetMapping("/orders")
    public String viewUserOrders(@RequestParam(defaultValue = "0") int page,
                                 HttpSession session,
                                 Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/user/login";

        Page<Order> orders = orderService.getUserOrders(user.getId(), page, 10);
        model.addAttribute("orders", orders.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());

        return "orders";
    }

    // ✅ View All Orders (Admin)
    @GetMapping("/allOrders")
    public String viewAllOrders(@RequestParam(defaultValue = "0") int page,
                                HttpSession session,
                                Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/user/login";

        Page<Order> orders = orderService.getAllOrders(page, 10);
        model.addAttribute("orders", orders.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orders.getTotalPages());

        return "allOrders";
    }

    // ✅ Cancel Order (User)
    @GetMapping("/cancel")
    public String cancelOrder(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        if (orderService.findOrder(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Order not found.");
            return "redirect:/order/orders";
        }

        orderService.cancelOrder(id);
        redirectAttributes.addFlashAttribute("success", "Order cancelled successfully.");
        return "redirect:/order/orders";
    }

    // ✅ Cancel Order (Admin)
    @GetMapping("/admincancel")
    public String cancelOrderByAdmin(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        if (orderService.findOrder(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Order not found.");
            return "redirect:/order/allOrders";
        }

        orderService.cancelOrder(id);
        redirectAttributes.addFlashAttribute("success", "Order cancelled by admin.");
        return "redirect:/order/allOrders";
    }

    // ✅ Mark Order as Packing
    @GetMapping("/packing")
    public String markPacking(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        if (orderService.findOrder(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Order not found.");
            return "redirect:/order/allOrders";
        }

        orderService.orderPacking(id);
        redirectAttributes.addFlashAttribute("success", "Order marked as Packing.");
        return "redirect:/order/allOrders";
    }

    // ✅ Mark Order as Out for Delivery
    @GetMapping("/outfordelivery")
    public String markOutForDelivery(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        if (orderService.findOrder(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Order not found.");
            return "redirect:/order/allOrders";
        }

        orderService.outForDelivery(id);
        redirectAttributes.addFlashAttribute("success", "Order marked as Out for Delivery.");
        return "redirect:/order/allOrders";
    }

    // ✅ Mark Order as Delivered
    @GetMapping("/delivered")
    public String markDelivered(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        if (orderService.findOrder(id).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Order not found.");
            return "redirect:/order/allOrders";
        }

        orderService.orderDelivered(id);
        redirectAttributes.addFlashAttribute("success", "Order marked as Delivered.");
        return "redirect:/order/allOrders";
    }

    // ✅ Submit Review
    @PostMapping("/submitReview")
    @ResponseBody
    public Map<String, Object> submitReview(@RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long orderId = Long.parseLong(data.get("orderId").toString());
            int rating = Integer.parseInt(data.get("rating").toString());
            String message = data.get("reviewMessage").toString();

            boolean success = reviewService.submitReview(orderId, rating, message);
            response.put("success", success);
            response.put("message", success ? "Review submitted." : "Review already exists or invalid order.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred while submitting review.");
        }
        return response;
    }

    // ✅ View All Reviews (Admin)
    @GetMapping("/viewReview")
    public String viewReview(Model model, RedirectAttributes redirectAttributes) {
        List<OrderReview> reviews = reviewService.getOrderReview();
        if (reviews.isEmpty()) {
            redirectAttributes.addFlashAttribute("noReviewId", "No reviews found.");
        } else {
            model.addAttribute("review", reviews);
        }
        return "viewReviews";
    }
}
