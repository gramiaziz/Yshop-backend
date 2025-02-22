package myshop.shop.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myshop.shop.Interface.ICategoryService;
import myshop.shop.Models.Category;
import myshop.shop.Repository.CategoryRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CategoryService implements ICategoryService {
    @Autowired
    CategoryRep categoryRepository;

    // Get all categories
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Get a category by ID
    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Create a new category
    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Update an existing category
    @Override
    public Category updateCategory(Long id, Category categoryDetails) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(categoryDetails.getName());
                    existingCategory.setDescription(categoryDetails.getDescription());
                    return categoryRepository.save(existingCategory);
                })
                .orElseThrow(() -> new RuntimeException("Category not found with id " + id));
    }

    // Delete a category
    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
