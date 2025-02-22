package myshop.shop.Repository;

import myshop.shop.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRep extends JpaRepository<Product, Long> {
}
