package myshop.shop.Repository;

import myshop.shop.Models.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBrandRep extends JpaRepository<ProductBrand, Long> {
}
