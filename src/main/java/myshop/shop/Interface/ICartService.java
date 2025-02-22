package myshop.shop.Interface;

import myshop.shop.Models.Cart;
import myshop.shop.Models.CartItem;
import myshop.shop.Models.CustomerOrder;
import myshop.shop.Models.OrderItem;

import java.util.List;
import java.util.Optional;

public  interface ICartService {

    Cart createCart(Cart cart);

    CartItem addCartItem(CartItem cartItem);

    List<Cart> getAllCarts();

    Optional<Cart> getCartById(Long id);

    Optional<Cart> getCartByUser();

    void deleteCart();

    CustomerOrder placeOrder(Long cartId);

    void confirmation(Long id);
}
