package myshop.shop.Service;

import myshop.shop.Interface.ICartService;
import myshop.shop.Models.*;
import myshop.shop.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ICartService {

    @Autowired
    private CartRep cartRepository;
    @Autowired
    private CustomerRep customerRep;
    @Autowired
    private CartItemRep cartItemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRep productRepository;
    @Autowired
    private OrderRep customerOrderRepository;

    @Autowired
    private OrderitemRep orderitemRep;

    @Override
    public Cart createCart(Cart cart) {
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart must contain at least one item.");
        }
        Optional<Customer> customerOptional = customerRep.findByUsername(userService.getLoggedInUsername());
        Customer customer = customerOptional.orElseThrow(() -> new RuntimeException("Must login before this action"));

        // Associate CartItems with Cart and calculate subtotal
        double totalPrice = 0.0;
        for (CartItem item : cart.getCartItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + item.getProduct().getId()));

            // Set the full product entity back into the CartItem
            item.setProduct(product);
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new RuntimeException("Cart items must have a positive quantity.");
            }
            if (item.getProduct() == null ) {
                throw new RuntimeException("Cart items must have a valid product.");
            }
            if (item.getProduct().getPrice() == 0.0) {
                throw new RuntimeException("Cart items must have a valid price.");
            }

            // Calculate the subTotal for each item
            double subTotal = item.getQuantity() * item.getProduct().getPrice();
            item.setSubTotal(subTotal);

            // Link CartItem to Cart
            item.setCart(cart);

            // Add subTotal to totalPrice
            totalPrice += subTotal;
        }

        // Set the total price and cart date for the Cart
        cart.setTotalAmount(cart.getTotalAmount()+totalPrice);
        cart.setCustomer(customer);

        // Save Cart (cascade will save CartItems)
        return cartRepository.save(cart);
    }


    @Override
    public CartItem addCartItem(CartItem cartItem) {
        Optional<Customer> customerOptional = customerRep.findByUsername(userService.getLoggedInUsername());
        Customer customer = customerOptional.orElseThrow(() -> new RuntimeException("Must login before this action"));
        Long cartId = customer.getCart().getId();

        Product product = productRepository.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + cartItem.getProduct().getId()));
        cartItem.setProduct(product);
        cartItem.setSubTotal(product.getPrice()*cartItem.getQuantity());

        double totalprice = cartItem.getQuantity() * cartItem.getProduct().getPrice();
        return cartRepository.findById(cartId).map(cart -> {
            cart.setTotalAmount(cart.getTotalAmount() + totalprice);
            cartItem.setCart(cart);

            return cartItemRepository.save(cartItem);
        }).orElseThrow(() -> new RuntimeException("Cart not found with id " + cartId));
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public Optional<Cart> getCartById(Long id) {
        return cartRepository.findById(id);
    }
    @Override
    public Optional<Cart> getCartByUser() {
        Optional<Customer> customerOptional = customerRep.findByUsername(userService.getLoggedInUsername());
        Customer customer = customerOptional.orElseThrow(() -> new RuntimeException("Must login before this action"));

        if (customer.getCart() == null) {
            throw new RuntimeException("Customer does not have a cart to delete.");
        }
        Long cartId = customer.getCart().getId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));
        return Optional.ofNullable(cart);
    }

    @Transactional
    @Override
    public void deleteCart() {
        Optional<Customer> customerOptional = customerRep.findByUsername(userService.getLoggedInUsername());
        Customer customer = customerOptional.orElseThrow(() -> new RuntimeException("Must login before this action"));
        Long cartId = customer.getCart().getId();
        if (customer.getCart() == null) {
            throw new RuntimeException("Customer does not have a cart to delete.");
        }
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));

        cartRepository.deleteById(cartId);
        cartRepository.flush();
        System.out.println("cart deleted ");
        Optional<Cart> deletedCart = cartRepository.findById(cartId);
        if (deletedCart.isPresent()) {
            System.out.println("Cart still exists in DB");
        } else {
            System.out.println("Cart deleted successfully from DB");
        }
    }

    @Transactional
    public CustomerOrder placeOrder(Long cartId) {
        // Fetch the cart by ID
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart must contain at least one item.");
        }
        Optional<Customer> customerOptional = customerRep.findByUsername(userService.getLoggedInUsername());
        Customer customer = customerOptional.orElseThrow(() -> new RuntimeException("Must login before this action"));
        // Create a new CustomerOrder from the cart's items
        CustomerOrder customerOrder = new CustomerOrder();
        double totalPrice = 0.0;

        // Loop through the cart items to create order items
        for (CartItem cartItem : cart.getCartItems()) {
            // Get the product from the cart item
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + cartItem.getProduct().getId()));

            // Create an OrderItem from the CartItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());

            orderItem.setSubTotal(cartItem.getQuantity() * product.getPrice());
            totalPrice += orderItem.getSubTotal();
            orderItem.setCustomerOrder(customerOrder);
            customerOrder.getOrderItems().add(orderItem);
        }

        // Set the total price and order date for the CustomerOrder
        customerOrder.setTotalAmount(totalPrice);
        customerOrder.setCustomer(customer);
        customerOrder.setOrderDate(LocalDateTime.now());
        customerOrder.setStatus("en attente");
        customerOrderRepository.save(customerOrder);

        cartRepository.delete(cart);
        cartRepository.flush();

        return customerOrder;
    }
    @Override
    public void confirmation(Long id){
        CustomerOrder c = customerOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("order not found with id: " + id));
        c.setStatus("confirmer");
        customerOrderRepository.save(c);
    }
    @Transactional
    public void deleteCartItem(Long cartItemId) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);

        if (cartItem.isPresent()) {
            cartItemRepository.deleteById(cartItemId);
            Cart cart = cartItem.get().getCart();
            cart.setTotalAmount(cart.getTotalAmount() - cartItem.get().getSubTotal());
            cartItemRepository.flush();
            System.out.println("Cart item deleted successfully.");
        } else {
            throw new RuntimeException("Cart item not found with ID: " + cartItemId);
        }
    }
}
