package com.anderson.cityshop.service;

import com.anderson.cityshop.dao.ICategoryDao;
import com.anderson.cityshop.model.Category;
import com.anderson.cityshop.response.CategoryResponseRest;
import com.anderson.cityshop.service.interfaces.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryDao categoryDao;

    public CategoryServiceImpl(ICategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * Searches and retrieves all categories from the database.
     * Constructs a response containing the list of categories and metadata
     * about the request status. Handles exceptions and returns an error response
     * in case of failures.
     *
     * @return a ResponseEntity containing a CategoryResponseRest object
     *         with the list of categories and relevant metadata.
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CategoryResponseRest> search() {
        CategoryResponseRest response = new CategoryResponseRest();
        try {
            List<Category> categories = (List<Category>) categoryDao.findAll();
            response.getCategoryResponse().setCategory(categories);
            response.setMetadata("Ok status", "00", "Success response");
        } catch (Exception e) {
            response.setMetadata("Bad status", "-1", "There was an error");
            e.getStackTrace();
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
    }

    /**
     * Searches for a category by its unique identifier (ID) in the database.
     *
     * @param id the unique identifier of the category to search for
     * @return a ResponseEntity containing a CategoryResponseRest object with the found category
     *         and additional metadata.
     */
    @Override
    @Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> searchById(Long id) {
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		
		try {
			
			Optional<Category> category = categoryDao.findById(id);
			
			if(category.isPresent()) {
				list.add(category.get());
				response.getCategoryResponse().setCategory(list);
				response.setMetadata("Ok status", "00", "Success response");
			} else {
				response.setMetadata("Bad response", "-1", "Category not found");
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			response.setMetadata("Bad response", "-1", "There was an error");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

    /**
     * Saves a category in the database.
     *
     * @param category the category object to be saved
     * @return a ResponseEntity containing the CategoryResponseRest object and the corresponding HTTP status
     */
    @Override
    @Transactional
    public ResponseEntity<CategoryResponseRest> save(Category category) {
        CategoryResponseRest response = new CategoryResponseRest();
        List<Category> list = new ArrayList<>();
        try {

            Category categorySaved = categoryDao.save(category);
            if(categorySaved != null) {
                list.add(categorySaved);
                response.getCategoryResponse().setCategory(list);
                response.setMetadata("Ok status", "00", "Category saved");
            } else {
                response.setMetadata("Bad response", "-1", "Category not saved");
                return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            response.setMetadata("Bad response", "-1", "There was an error");
            e.getStackTrace();
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
    }

    /**
     * Updates an existing category based on the provided category object and ID.
     *
     * @param category the category object containing the updated information
     * @param id the ID of the category to be updated
     * @return a ResponseEntity containing the CategoryResponseRest object with the update status
     */
    @Override
    @Transactional
    public ResponseEntity<CategoryResponseRest> update(Category category, Long id) {
        CategoryResponseRest response = new CategoryResponseRest();
        List<Category> list = new ArrayList<>();

        try {

            Optional<Category> categorySearch = categoryDao.findById(id);

            if (categorySearch.isPresent()) {
                // update doc
                categorySearch.get().setName(category.getName());
                categorySearch.get().setDescription(category.getDescription());

                Category categoryToUpdate = categoryDao.save(categorySearch.get());

                if (categoryToUpdate != null) {
                    list.add(categoryToUpdate);
                    response.getCategoryResponse().setCategory(list);
                    response.setMetadata("Ok status", "00", "Category updated");
                } else {
                    response.setMetadata("Bad response", "-1", "Category not updated");
                    return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.BAD_REQUEST);
                }

            } else {
                // send error to user
                response.setMetadata("Bad response", "-1", "Category not found");
                return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            response.setMetadata("Bad response", "-1", "There was an error updating the category");
            e.getStackTrace();
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
    }

    /**
     * Deletes a category by its identifier.
     *
     * @param id the identifier of the category to be deleted
     * @return a ResponseEntity containing the CategoryResponseRest object with
     *         metadata indicating success or failure of the deletion operation
     */
    @Override
    public ResponseEntity<CategoryResponseRest> deleteById(Long id) {
        CategoryResponseRest response = new CategoryResponseRest();

        try {

            categoryDao.deleteById(id);
            response.setMetadata("Ok status", "00", "Category deleted");

        } catch (Exception e) {
            response.setMetadata("Bad response", "-1", "There was an error deleting the category");
            e.getStackTrace();
            return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
    }
}
