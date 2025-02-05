package com.anderson.cityshop.service;

import com.anderson.cityshop.dao.ICategoryDao;
import com.anderson.cityshop.dao.IProductDao;
import com.anderson.cityshop.model.Category;
import com.anderson.cityshop.model.Product;
import com.anderson.cityshop.response.ProductResponseRest;
import com.anderson.cityshop.service.interfaces.IProductService;
import com.anderson.cityshop.utils.InventoryUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl  implements IProductService {

    private final ICategoryDao categoryDao;
    private final IProductDao productDao;

    public ProductServiceImpl(ICategoryDao categoryDao, IProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }


    @Override
    @Transactional
    public ResponseEntity<ProductResponseRest> save(Product product, Long CategoryId) {
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try {
            // search category to set in the product object
            Optional<Category> category = categoryDao.findById(CategoryId);
            if(category.isPresent()) {
                product.setCategory(category.get());
            } else {
                response.setMetadata("Error consulting", "-1", "No category associated to the product");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }

            // save product
            Product productSaved = productDao.save(product);

            if (productSaved != null) {
                list.add(productSaved);
                response.getProductResponse().setProducts(list);
                response.setMetadata("Ok", "00", "Product saved");
            } else {
                response.setMetadata("Bad Error", "-1", "Product not saved");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e){
            e.getStackTrace();
            response.setMetadata("Bad Error", "-1", "Error saving product");
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductResponseRest> searchById(Long id) {

        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try {
            // search product by id
            Optional<Product> product = productDao.findById(id);
            if(product.isPresent()) {

                byte[] decompressedImage = InventoryUtils.decompressZLib(product.get().getPicture());
                product.get().setPicture(decompressedImage);
                list.add(product.get());
                response.getProductResponse().setProducts(list);
                response.setMetadata("Ok", "00", "Product found");

            } else {
                response.setMetadata("Error consulting", "-1", "Product not found");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e){
            e.getStackTrace();
            response.setMetadata("Bad Error", "-1", "Error saving product");
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }
}
