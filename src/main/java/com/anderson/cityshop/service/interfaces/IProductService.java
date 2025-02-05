package com.anderson.cityshop.service.interfaces;

import com.anderson.cityshop.model.Product;
import com.anderson.cityshop.response.ProductResponseRest;
import org.springframework.http.ResponseEntity;

public interface IProductService{
    public ResponseEntity<ProductResponseRest> save(Product product, Long CategoryId);
    public ResponseEntity<ProductResponseRest> searchById(Long id);
}
