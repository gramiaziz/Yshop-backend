package myshop.shop.Interface;

import myshop.shop.Models.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    // Get all categories
    List<Category> getAllCategories();

    // Get a category by ID
    Optional<Category> getCategoryById(Long id);

    // Create a new category
    Category createCategory(Category category);

    // Update an existing category
    Category updateCategory(Long id, Category categoryDetails);

    // Delete a category
    void deleteCategory(Long id);
}
