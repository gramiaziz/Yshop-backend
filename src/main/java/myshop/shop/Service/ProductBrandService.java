package myshop.shop.Service;

import myshop.shop.Interface.IProductBrand;
import myshop.shop.Models.ProductBrand;
import myshop.shop.Repository.ProductBrandRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public class ProductBrandService {
    @Service
    public class BrandService implements IProductBrand {

        @Autowired
         ProductBrandRep productBrandRepository;

        @Override
        public List<ProductBrand> getAllProductBrands() {
            return productBrandRepository.findAll();
        }

        @Override
        public Optional<ProductBrand> getProductBrandById(Long id) {
            return productBrandRepository.findById(id);
        }

        @Override
        public ProductBrand createProductBrand(ProductBrand productBrand) {
            return productBrandRepository.save(productBrand);
        }

        @Override
        public ProductBrand updateProductBrand(Long id, ProductBrand productBrandDetails) {
            return productBrandRepository.findById(id)
                    .map(existingProductBrand -> {
                        existingProductBrand.setName(productBrandDetails.getName());
                        existingProductBrand.setLogo(productBrandDetails.getLogo());
                        existingProductBrand.setOriginCountry(productBrandDetails.getOriginCountry());
                        return productBrandRepository.save(existingProductBrand);
                    })
                    .orElseThrow(() -> new RuntimeException("ProductBrand not found with id " + id));
        }

        @Override
        public void deleteProductBrand(Long id) {
            productBrandRepository.deleteById(id);
        }
    }
}
