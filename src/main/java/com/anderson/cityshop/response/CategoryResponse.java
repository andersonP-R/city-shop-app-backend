package com.anderson.cityshop.response;

import com.anderson.cityshop.model.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse {
    private List<Category> category;
}