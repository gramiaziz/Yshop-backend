package myshop.shop.Repository;

import myshop.shop.Models.Category;
import myshop.shop.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRep extends JpaRepository<Category, Long> {
}
