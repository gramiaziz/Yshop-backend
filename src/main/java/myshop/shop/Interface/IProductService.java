package myshop.shop.Interface;

import myshop.shop.Models.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IProductService {


    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);


    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
