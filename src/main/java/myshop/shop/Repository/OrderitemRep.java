package myshop.shop.Repository;

import myshop.shop.Models.Customer;
import myshop.shop.Models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderitemRep extends JpaRepository<OrderItem, Long> {
}
