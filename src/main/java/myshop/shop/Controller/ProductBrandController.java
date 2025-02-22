package myshop.shop.Controller;

import myshop.shop.Interface.IProductBrand;
import myshop.shop.Models.ProductBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product-brands")
public class ProductBrandController {
    IProductBrand iBrandService;

    // GET all ProductBrands
    @GetMapping
    public List<ProductBrand> getAllProductBrands() {
        return iBrandService.getAllProductBrands();
    }

    // GET a ProductBrand by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductBrand> getProductBrandById(@PathVariable Long id) {
        Optional<ProductBrand> productBrand = iBrandService.getProductBrandById(id);
        return productBrand.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST: Create a new ProductBrand
    @PostMapping
    public ProductBrand createProductBrand(@RequestBody ProductBrand productBrand) {
        return iBrandService.createProductBrand(productBrand);
    }

    // PUT: Update an existing ProductBrand
    @PutMapping("/{id}")
    public ResponseEntity<ProductBrand> updateProductBrand(@PathVariable Long id, @RequestBody ProductBrand productBrandDetails) {
        try {
            ProductBrand updatedProductBrand = iBrandService.updateProductBrand(id, productBrandDetails);
            return ResponseEntity.ok(updatedProductBrand);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: Delete a ProductBrand
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductBrand(@PathVariable Long id) {
        iBrandService.deleteProductBrand(id);
        return ResponseEntity.noContent().build();
    }
}
