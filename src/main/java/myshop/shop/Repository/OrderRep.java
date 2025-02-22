package myshop.shop.Repository;

import myshop.shop.Models.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRep extends JpaRepository<CustomerOrder, Long> {
}
