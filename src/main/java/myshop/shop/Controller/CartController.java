package myshop.shop.Controller;

import myshop.shop.Models.Cart;
import myshop.shop.Models.CartItem;
import myshop.shop.Models.CustomerOrder;
import myshop.shop.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // 1. Create a new cart
    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        try {
            Cart newCart = cartService.createCart(cart);
            return ResponseEntity.ok(newCart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/additem")
    public ResponseEntity<CartItem> addCartItem(@RequestBody CartItem cartItem) {
        try {
            CartItem newCartItem = cartService.addCartItem( cartItem);
            return ResponseEntity.ok(newCartItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 3. Get all carts
    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();
        return ResponseEntity.ok(carts);
    }

    // 4. Get a cart by ID
    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long cartId) {
        Optional<Cart> cart = cartService.getCartById(cartId);
        return cart.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/usercart")
    public ResponseEntity<Cart> getCartByUser() {
        Optional<Cart> cart = cartService.getCartByUser();
        return cart.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @DeleteMapping("/deletecart")
    public ResponseEntity<Void> deleteCart() {
        try {
            cartService.deleteCart();
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/place-order/{cartId}")
    public ResponseEntity<CustomerOrder> placeOrder(@PathVariable Long cartId) {
        try {
            CustomerOrder customerOrder = cartService.placeOrder(cartId);
            return ResponseEntity.ok(customerOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PutMapping("/confirmation/{id}")
    public ResponseEntity<Boolean> confirmationdordre(@PathVariable Long id){
        try {
            cartService.confirmation(id);
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCartItem(@PathVariable Long id) {
        try {
            cartService.deleteCartItem(id);
            return ResponseEntity.ok("Cart item deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
