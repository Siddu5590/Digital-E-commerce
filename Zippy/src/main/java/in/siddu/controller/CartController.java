package in.siddu.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.siddu.entity.CartItem;
import in.siddu.entity.Product;
import in.siddu.entity.User;
import in.siddu.repository.CartItemRepository;
import in.siddu.repository.ProductRepo;
import in.siddu.repository.UserRepo;
import in.siddu.service.ProductService;
import in.siddu.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductService pservice;

    @Autowired
    private CartItemRepository cartRepo;

    @Autowired
    private UserService uservice;

    @Autowired
    private UserRepo urepo;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        String email = (String) session.getAttribute("umail");
        if (email == null) {
            return "redirect:/login"; // Redirect if the user is not logged in
        }
        
        User user = urepo.findByEmail(email);
        List<CartItem> cartItems = cartRepo.findByUser(user);

        // Calculate the total price of all items in the cart
        double total = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
        
        double discount = 0;
        double discountedTotal = total;

        // Apply 10% discount if total price is greater than 1000
        if (total > 1000) {
            discount = total * 0.10;
            discountedTotal = total - discount;
        }

        // Add attributes to the model for rendering in the view
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("discount", discount);
        model.addAttribute("discountedTotal", discountedTotal);
        model.addAttribute("user", user);

        return "cart";
    }


    @PostMapping("/add-to-cart/{id}")
    @ResponseBody
    public String addToCart(@PathVariable Integer id, HttpSession session) {
        String email = (String) session.getAttribute("umail");
        if (email == null) return "0";

        User user = urepo.findByEmail(email);
        Product product = pservice.getProduct(id);

        CartItem item = cartRepo.findByUserAndProduct(user, product).orElse(new CartItem());
        item.setUser(user);
        item.setProduct(product);

        int currentQty = item.getQuantity();
        if (currentQty < 10) {
            item.setQuantity(currentQty + 1);
            cartRepo.save(item);
        }

        long count = cartRepo.countByUser(user);
        return String.valueOf(count);
    }


    @PostMapping("/cart/increase/{productId}")
    @ResponseBody
    public String increaseQty(@PathVariable Integer productId, HttpSession session) {
        String email = (String) session.getAttribute("umail");
        if (email == null) return "0";

        User user = urepo.findByEmail(email);
        Product product = pservice.getProduct(productId);

        Optional<CartItem> optionalItem = cartRepo.findByUserAndProduct(user, product);
        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            int currentQty = item.getQuantity();
            if (currentQty < 10) {
                item.setQuantity(currentQty + 1);
                cartRepo.save(item);
                return String.valueOf(item.getQuantity());
            } else {
                return "max"; // return "max" if quantity exceeds the limit
            }
        }
        return "0";
    }

    @PostMapping("/cart/decrease/{productId}")
    @ResponseBody
    public String decreaseQty(@PathVariable Integer productId, HttpSession session) {
        String email = (String) session.getAttribute("umail");
        if (email == null) return "0";

        User user = urepo.findByEmail(email);
        Product product = pservice.getProduct(productId);

        Optional<CartItem> optionalItem = cartRepo.findByUserAndProduct(user, product);
        if (optionalItem.isPresent()) {
            CartItem item = optionalItem.get();
            int qty = item.getQuantity();
            if (qty > 1) {
                item.setQuantity(qty - 1);
                cartRepo.save(item);
                return String.valueOf(item.getQuantity());
            } else {
                cartRepo.delete(item); // Delete item if quantity becomes 0
                return "0";
            }
        }
        return "0";
    }


    @GetMapping("/cart/count")
    @ResponseBody
    public long cartCount(HttpSession session) {
        String email = (String) session.getAttribute("umail");
        if (email == null) return 0;
        User user = urepo.findByEmail(email);
        return cartRepo.countByUser(user);
    }

    @PostMapping("/cart/remove/{id}")
    public String removeItem(@PathVariable Long id, HttpSession session) {
        cartRepo.deleteById(id);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("umail");
        if (email == null) {
            redirectAttributes.addFlashAttribute("error", "User not logged in!");
            return "redirect:/login";
        }

        User user = urepo.findByEmail(email);
        List<CartItem> cartItems = cartRepo.findByUser(user);

        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Cart is empty!");
            return "redirect:/cart";
        }

        // Simulate order save (optional)
        cartRepo.deleteAll(cartItems);
        redirectAttributes.addFlashAttribute("success", "Checkout successful! Thank you for your purchase.");
        return "redirect:/cart";
    }
}
