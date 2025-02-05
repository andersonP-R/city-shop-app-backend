package com.anderson.cityshop.response;

import com.anderson.cityshop.model.Product;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {
    List<Product> products;
}
