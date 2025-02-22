package myshop.shop.Repository;

import myshop.shop.Models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRep extends JpaRepository<Admin, Long> {
}
