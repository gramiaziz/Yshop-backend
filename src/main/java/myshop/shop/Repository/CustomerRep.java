package myshop.shop.Repository;

import myshop.shop.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRep extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);


}
