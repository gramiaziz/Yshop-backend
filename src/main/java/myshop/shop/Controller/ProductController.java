package myshop.shop.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import myshop.shop.Interface.IProductService;
import myshop.shop.Models.Product;
import myshop.shop.Service.ProductService;
import myshop.shop.Service.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

     @Autowired
     IProductService productService;

    @GetMapping("/getall")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

   /* @PostMapping("/add")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);

    }*/

    @PostMapping("/add")
    public Product createProductwithimage(HttpServletRequest request,@RequestParam String productJson, @RequestParam MultipartFile file) throws IOException, SerialException, SQLException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(productJson, Product.class);
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);


        product.setImage(blob);
        return productService.createProduct(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
