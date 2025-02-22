package myshop.shop.Service;

import myshop.shop.Interface.IProductService;
import myshop.shop.Models.Product;
import myshop.shop.Repository.ProductRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    @Autowired
    ProductRep productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        // Save the product with the image URL
        return productRepository.save(product);
    }


    @Transactional
    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setStock(product.getStock());
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }


    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }
}
