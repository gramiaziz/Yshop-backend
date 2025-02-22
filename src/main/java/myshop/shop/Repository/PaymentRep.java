package myshop.shop.Repository;

import myshop.shop.Models.Customer;
import myshop.shop.Models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRep extends JpaRepository<Payment, Long> {
}
