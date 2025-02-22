package myshop.shop.Repository;

import myshop.shop.Models.Cart;
import myshop.shop.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRep extends JpaRepository<Cart, Long> {
}
