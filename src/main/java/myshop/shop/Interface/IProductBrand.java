package myshop.shop.Interface;

import myshop.shop.Models.ProductBrand;

import java.util.List;
import java.util.Optional;

public interface IProductBrand {
    List<ProductBrand> getAllProductBrands();

    Optional<ProductBrand> getProductBrandById(Long id);

    ProductBrand createProductBrand(ProductBrand productBrand);

    ProductBrand updateProductBrand(Long id, ProductBrand productBrandDetails);

    void deleteProductBrand(Long id);
}
