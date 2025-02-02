package com.anderson.cityshop.controller;

import com.anderson.cityshop.model.Category;
import com.anderson.cityshop.response.CategoryResponseRest;
import com.anderson.cityshop.service.interfaces.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class CategoryRestController {

    private final ICategoryService categoryService;

    public CategoryRestController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Searches for all categories.
     *
     * @return a ResponseEntity containing the CategoryResponseRest object,
     *         which includes a list of all available categories and related metadata
     */
    @GetMapping("/categories")
    public ResponseEntity<CategoryResponseRest> searchCategories() {
        return categoryService.search();
    }

    /**
     * Saves a new category to the system.
     *
     * @param category the category object containing the details to be saved
     * @return a ResponseEntity containing the CategoryResponseRest object,
     *         which includes the saved category details and metadata
     */
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponseRest> save(@RequestBody Category category) {
        return categoryService.save(category);
    }

    /**
     * Searches for a category by its ID.
     *
     * @param id the unique identifier of the category to search for
     * @return a ResponseEntity containing the CategoryResponseRest object,
     *         which includes the details of the requested category
     */
    @GetMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> searchCategoriesById(@PathVariable Long id) {
		return categoryService.searchById(id);
	}

    /**
     * Updates an existing category in the system.
     *
     * @param category the details of the category to be updated
     * @param id the unique identifier of the category to be updated
     * @return a ResponseEntity containing the CategoryResponseRest object,
     *         which includes the updated category details and metadata
     */
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseRest> update(@RequestBody Category category, @PathVariable Long id) {
        return categoryService.update(category, id);
    }

    /**
     * Deletes a category identified by its unique ID.
     *
     * @param id the unique identifier of the category to be deleted
     * @return a ResponseEntity containing the CategoryResponseRest object,
     *         which provides information about the success or failure of the deletion operation
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseRest> deleteById(@PathVariable Long id) {
        return categoryService.deleteById(id);
    }

}
