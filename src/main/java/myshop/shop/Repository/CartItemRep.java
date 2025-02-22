package myshop.shop.Repository;

import myshop.shop.Models.CartItem;
import myshop.shop.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRep extends JpaRepository<CartItem, Long> {
}
