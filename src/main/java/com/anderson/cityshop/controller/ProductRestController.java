package com.anderson.cityshop.controller;

import com.anderson.cityshop.model.Product;
import com.anderson.cityshop.response.ProductResponseRest;
import com.anderson.cityshop.service.interfaces.IProductService;
import com.anderson.cityshop.utils.InventoryUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ProductRestController {

    private final IProductService productService;

    public ProductRestController(IProductService productService) {
        this.productService = productService;
    }

    /**
     * Saves a new product with the given details and associates it with a specified category.
     * The product picture is compressed before being saved.
     *
     * @param picture the picture file of the product to be saved
     * @param name the name of the product
     * @param price the price of the product
     * @param quantity the quantity of the product in stock
     * @param categoryID the ID of the category to associate the product with
     * @return a ResponseEntity containing a ProductResponseRest object which includes the details of the saved product
     */
    @PostMapping("/products")
    public ResponseEntity<ProductResponseRest> save(
            @RequestParam("picture")MultipartFile picture,
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("quantity") int quantity,
            @RequestParam("categoryId") Long categoryID
    )
    {
        try {
            Product product = new Product();
            product.setName(name);
            product.setQuantity(quantity);
            product.setPrice(price);
            // Compression of the image
            product.setPicture(InventoryUtils.compressZLib(picture.getBytes()));

            return productService.save(product, categoryID);
        } catch (IOException e) {
            // Handle IOException
            // Log the error and return a bad request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Replace with an appropriate error response object if needed
        } catch (TransactionSystemException ex) {
            // If there are transaction-related issues
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Replace with additional error details if needed
        }

    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the unique identifier of the product to search for
     * @return a ResponseEntity containing the ProductResponseRest object,
     *         which includes the details of the requested product
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseRest> searchById(@PathVariable Long id) {
        return productService.searchById(id);
    }

}